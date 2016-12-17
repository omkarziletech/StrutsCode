<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil,java.text.SimpleDateFormat,com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@ page import="com.gp.cvst.logisoft.util.*,com.gp.cvst.logisoft.beans.*,com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.domain.User,java.util.*,com.gp.cvst.logisoft.hibernate.dao.QuotationDAO,com.gp.cong.logisoft.bc.ratemanagement.PortsBC,org.apache.commons.lang3.StringUtils"%>
<%@ page import="com.gp.cong.logisoft.hibernate.dao.LogFileEdiDAO"%>
<jsp:directive.page import="com.gp.cong.common.CommonConstants"/>
<jsp:directive.page import="java.text.DateFormat"/>
<jsp:directive.page import="com.gp.cong.logisoft.bc.fcl.QuotationConstants"/>
<jsp:directive.page import="com.gp.cong.logisoft.util.StringFormatter"/>
<jsp:directive.page import="com.logiware.constants.ItemConstants"/>
<%@ page import="com.gp.cong.logisoft.bc.fcl.ImportBc"%>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@include file="../includes/jspVariables.jsp" %>
<script src="${path}/js/jquery/jquery.js" type="text/javascript" ></script>
<bean:define id="fileNumberPrefix" type="String">
    <bean:message key="fileNumberPrefix"/>
</bean:define>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<html:html locale="true">
    <head>
        <style>
            .linkSpan{
                cursor:pointer;
                text-decoration:underline;
            }
            #attachListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 25%;
                top: 15%;
                _height: expression(document.body.offsetHeight + "px");
            }
            #originAndDestinationDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid;
                border-width:2px;
                border-color:#808080;
                padding:0px 0px 0px 0px;
                background-color: #FFFFFF;
                left:10px;
                right:5px;
                top:0;
                margin:0 auto;
            }

        </style>
        <title> Search Quotation</title>
        <%!
        String fromDate = null;
        String toDate = null;
        StringFormatter stringFormatter = new StringFormatter();
        %>
        <%
          String loadString="";
          boolean importFlg=(null!=session.getAttribute(ImportBc.sessionName))?true:false;
                if(null !=session.getAttribute("autoClick") && session.getAttribute("autoClick").equals("autoClick")){
                loadString=(String)session.getAttribute("autoClick");
        }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                Calendar calendar = Calendar.getInstance();
                
                toDate=simpleDateFormat.format(calendar.getTime());
                int monthId=calendar.get(calendar.MONTH);
                if(importFlg){
                  calendar.add(calendar.MONTH,-6);  
                } else {
                  calendar.roll(calendar.MONTH, false);// way to substract month //To add 1 month calendar.roll(calendar.MONTH, true);   
                }
                if(monthId==0){
                  calendar.roll(calendar.YEAR, false);// roll down year by 1 when month is January of next year.
                }
                fromDate =simpleDateFormat.format(calendar.getTime());
        String path = request.getContextPath();
        String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
        String link="";
        String editPath=path+"/searchquotation.do";

        List displayId=(List)session.getAttribute(QuotationConstants.FILESEARCHLIST);
        PortsBC portsBC = new PortsBC();

        if(request.getParameter("programid")!=null){
        String programId=request.getParameter("programid");
          session.setAttribute("processinfoforquotation",programId);
        }
        DBUtil dbUtil=new DBUtil();
        request.setAttribute("limitList", dbUtil.limitList());

        String fclFolderId="",quoteFolderId="",bookingFolderId="";
                        String fclLinkId="",quoteLinkId="",bookingLinkId="";
                        //getting unique item code from session
                        // Map session Object will return Unique code as a Key and folder and link id as a Value;
                        if(session.getAttribute(CommonConstants.UNIQUE_ITEM)!=null){
                        Map addUniqueCode = (Map)session.getAttribute(CommonConstants.UNIQUE_ITEM);
                        Set set=addUniqueCode.entrySet();
                                        for(Iterator it = set.iterator();it.hasNext();){
                                            Map.Entry entry = (Map.Entry )it.next();
                                           if(null!=session.getAttribute(ImportBc.sessionName)){
                                           // for import quote,booking and BL
                                                         if(entry.getKey()!=null && entry.getKey().equals(CommonConstants.QUOTE_CODE_IMP)){
                                                                 quoteFolderId=(String)entry.getValue();//QOT
                                                                 quoteLinkId=quoteFolderId.substring(quoteFolderId.lastIndexOf("=")+1);//link id
                                                    }
                                                    else if(entry.getKey()!=null &&  entry.getKey().equals(CommonConstants.BOOKING_CODE_IMP)){
                                                                 bookingFolderId=(String)entry.getValue();//BOK
                                                                 bookingLinkId=bookingFolderId.substring(bookingFolderId.lastIndexOf("=")+1);//link id
					    
                                                    }else if(entry.getKey()!=null &&  entry.getKey().equals(CommonConstants.FCL_CODE_IMP)){
                                                                    fclFolderId=(String)entry.getValue();//FCL
                                                                    fclLinkId=fclFolderId.substring(fclFolderId.lastIndexOf("=")+1);//link idd
                                                    }
                                                    }
                                                    else{
                                                    if(entry.getKey()!=null && entry.getKey().equals(CommonConstants.QUOTE_CODE)){
                                                                 quoteFolderId=(String)entry.getValue();//QOT
                                                                 quoteLinkId=quoteFolderId.substring(quoteFolderId.lastIndexOf("=")+1);//link id
                                                    }
                                                    else if(entry.getKey()!=null &&  entry.getKey().equals(CommonConstants.BOOKING_CODE)){
                                                                 bookingFolderId=(String)entry.getValue();//BOK
                                                                 bookingLinkId=bookingFolderId.substring(bookingFolderId.lastIndexOf("=")+1);//link id
					    
                                                    }else if(entry.getKey()!=null &&  entry.getKey().equals(CommonConstants.FCL_CODE)){
                                                                    fclFolderId=(String)entry.getValue();//FCL
                                                                    fclLinkId=fclFolderId.substring(fclFolderId.lastIndexOf("=")+1);//link idd
                                                    }
                                            }
                                        }
                        }
        request.setAttribute("regions", portsBC.getAllRegion1());

        String loginName="";
        Integer usuerId=0;
        User user11=new User();
                if(session.getAttribute("loginuser")!=null){
                  user11=(User)session.getAttribute("loginuser");
                  loginName = user11.getLoginName();
                  usuerId=user11.getUserId();
                }               
        %>

        <script>

            function addquote(page){
                if(page=='quote'){
                    window.parent.changeChilds('quoteId=0');
                }else if(page=='booking'){
                    window.parent.changeChilds('bookingId=0');
                }else if(page=='fclBl'){
                    window.parent.changeChilds('blId=0');
                }
            }
             
            function getClient(){
                var path="";
                if(document.getElementById('clientCheckbox').checked){
                    document.getElementById('clientCheckbox').value = 'on';
                    document.getElementById('client').value="";
                    path="&from=5&isDojo=false&check=true";
                }else{
                    document.getElementById('client').value="";
                    path="&from=5&isDojo=false&check=false";
                }
                appendEncodeUrl(path);
            }
            function seachByFromSailDate(){
                if(document.getElementById('fromSailDateCheck').checked){
                    document.getElementById('fromSailDateCheck').value = "on";
                }
            }

        </script>

        <%@include file="../includes/baseResources.jsp" %>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/ProcessInfoBC.js'></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type='text/javascript' src='${path}/dwr/interface/QuoteDwrBC.js'></script>
        <script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/autocomplete.js"></script>
        <script type="text/javascript" src="<%=path%>/js/fcl/fileNumber.js"></script>
        <script type="text/javascript" src="<%=path%>/js/tooltip/tooltip.js" ></script>
        <script language="javascript" src="${path}/js/rates.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="<%=path%>/js/caljs/CalendarPopup.js"></script>
        <script  language="javascript" type="text/javascript">
            function autoClick(){
                if(null != document.getElementById('converted') && undefined != document.getElementById('converted')){
                    document.getElementById('converted').click();
                }
            }
            function QuoteCopied(){
                if(null != document.getElementById('quoteCopied') && undefined != document.getElementById('quoteCopied')){
                    document.getElementById('quoteCopied').click();
                }
            }
            function showRatesPopUp(){
                document.getElementById('quickRates').disabled = true;
                DwrUtil.getQuickRates(function(data){
                    showPopUp();
                    var attachListDiv = createHTMLElement("div","attachListDiv","50%","50%",document.body);
                    dwr.util.setValue("attachListDiv", data, { escapeHtml:false });
                    initOPSAutocomplete("portofDischarge","destination_port_choices","","",
                    rootPath+"/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=7","");
                    initOPSAutocomplete("isTerminal","isTerminal_choices","","",
                    rootPath+"/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=6&typeOfmove=&isDojo=false","");
                    initAutocomplete("commcode","commcode_choices","","commcode_check",
                    rootPath+"/actions/getChargeCode.jsp?tabName=QUOTE&isDojo=false","");
                });
            }
            function getQuickRates(){
                document.getElementById('quickRates').disabled = false;
                var destinationPort = document.fclQuotesPopupForm.portofDischarge.value;
                var originPort = "";
                if(trim(document.fclQuotesPopupForm.isTerminal.value)!=""){
                    originPort = document.fclQuotesPopupForm.isTerminal.value;
                }
                if (!window.focus)return true;
                var haz="";
                if(document.fclQuotesPopupForm.hazmat[0].checked){
                    haz="Y";
                }else{
                    haz="N";
                }
                if((trim(document.fclQuotesPopupForm.portofDischarge.value) == "" && trim(document.fclQuotesPopupForm.isTerminal.value)=="")){
                    alertNew("PLEASE SELECT DESTINATION PORT OR ORIGIN");
                    return;
                }else if(trim(document.fclQuotesPopupForm.commcode.value) == ""){
                    alertNew("PLEASE SELECT COMMODITY CODE");
                    return;
                }
                document.getElementById('getRates').disabled = true;
                if((trim(document.fclQuotesPopupForm.portofDischarge.value) == "" || trim(document.fclQuotesPopupForm.isTerminal.value)=="")){
                    var searchBy = "city";
                    if(trim(document.fclQuotesPopupForm.portofDischarge.value) != "") {
                        if(!document.getElementById('destinationCity').checked){
                            searchBy="country";
                        }
                        QuoteDwrBC.getQuickRatesOriginsForDestination(document.fclQuotesPopupForm.isTerminal.value,document.fclQuotesPopupForm.portofDischarge.value,searchBy,function(data){
                            if(null == data || null == data[0] || trim(data[0])==""){
                                alert("No Data Found!");
                            }else if(data[0].length > 100) {
                                setDistance(data[1], data[0]);
                            }else {
                                document.fclQuotesPopupForm.isTerminal.value = data[0];
                                document.fclQuotesPopupForm.isTerminal_check.value = data[0];
                                openGetRates(haz, '');
                            }
                        });
                    }else {
                        if(!document.getElementById('originCountry').checked){
                            searchBy="country";
                        }
                        QuoteDwrBC.getDestinationsForOrigin(document.fclQuotesPopupForm.portofDischarge.value,document.fclQuotesPopupForm.isTerminal.value,searchBy,function(data){
                            if(data.length > 100) {
                                showOriginDestinationList(data);
                            }else {
                                document.fclQuotesPopupForm.portofDischarge.value = data;
                                document.fclQuotesPopupForm.portofDischarge_check.value = data;
                                openGetRates(haz, '');
                            }
                        });
                    }
                }else if(document.getElementById("showAllCity") &&  document.getElementById("showAllCity").checked) {
                    QuoteDwrBC.getAllCountryPorts(document.fclQuotesPopupForm.portofDischarge.value, function(data){
                        var origin = null;
                        var arry = null;
                        var passedValue = null;
                        if(null != data && data.length > 1){
                            passedValue = data.split('||');
                            var allCity = new Array();
                            for(i=0;i<passedValue.length;i++) {
                                arry = passedValue[i].split('==');
                                if(null == origin) {
                                    origin = arry[0];
                                    allCity[i] = arry[1];
                                }else {
                                    origin = origin + ',' +arry[0];
                                    allCity[i] = arry[1];
                                }
                            }
                            url = '<%=path%>/rateGrid.do?action=Origin&origin='+document.fclQuotesPopupForm.isTerminal.value+
                                "&destination="+origin+"&commodity="+document.fclQuotesPopupForm.commcode.value+'&hazardous='+haz+"&ratesFrom=quickRates"+"&destinationPort="+destinationPort+"&originPort="+originPort;
                            GB_show('FCL Rates Comparison Grid',url,document.body.offsetHeight-20,document.body.offsetWidth-100);
                        }else{
                            openGetRates(haz, '');
                        }
                    });
                }else {
                    QuoteDwrBC.getAllSynonymousCity(document.fclQuotesPopupForm.portofDischarge.value,document.fclQuotesPopupForm.isTerminal.value, function(data){
                        if(null != data){
                            if(null != data[0] && data[0] != '' && data[0].split(",").length > 1){
                                if(null != data[1] && data[1] != '' && data[1].split(",").length > 1){
                                    url = '<%=path%>/rateGrid.do?action=Origin&origin='+data[1]+
                                        "&destination="+data[0]+"&originName="+document.fclQuotesPopupForm.isTerminal.value+"&commodity="+document.fclQuotesPopupForm.commcode.value+'&hazardous='+haz+"&ratesFrom=quickRates"
                                    GB_show('FCL Rates Comparison Grid',url,document.body.offsetHeight-20,document.body.offsetWidth-100);
                                }else{
                                    url = '<%=path%>/rateGrid.do?action=Origin&origin='+document.fclQuotesPopupForm.isTerminal.value+
                                        "&destination="+data[0]+"&commodity="+document.fclQuotesPopupForm.commcode.value+'&hazardous='+haz+"&ratesFrom=quickRates"+"&destinationPort="+destinationPort+"&originPort="+originPort;
                                    GB_show('FCL Rates Comparison Grid',url,document.body.offsetHeight-20,document.body.offsetWidth-100);
                                }
                            }else if(null != data[1] && data[0] != '' && data[1].split(",").length > 1){
                                url = '<%=path%>/rateGrid.do?action=Origin&origin='+data[1]+
                                    "&destination="+document.fclQuotesPopupForm.portofDischarge.value+"&originName="+document.fclQuotesPopupForm.isTerminal.value+"&commodity="+document.fclQuotesPopupForm.commcode.value+'&hazardous='+haz+"&ratesFrom=quickRates"+"&destinationPort="+destinationPort+"&originPort="+originPort;
                                GB_show('FCL Rates Comparison Grid',url,document.body.offsetHeight-20,document.body.offsetWidth-100);
                            }else{
                                openGetRates(haz, '');
                            }
                        }else{
                            openGetRates(haz, '');
                        }
                    });
                }
            }
            function openGetRates(haz, val) {
                var destinationPort = document.fclQuotesPopupForm.portofDischarge.value;
                var originPort = "";
                if(trim(document.fclQuotesPopupForm.isTerminal.value)!=""){
                    originPort = document.fclQuotesPopupForm.isTerminal.value;
                }
                url = '<%=path%>/rateGrid.do?action=getrates&origin='+document.fclQuotesPopupForm.isTerminal.value+
                    "&destination="+document.fclQuotesPopupForm.portofDischarge.value+"&commodity="+document.fclQuotesPopupForm.commcode.value
                    +"&hazardous="+haz+"&fileNo="+val+"&ratesFrom=quickRates"+"&destinationPort="+destinationPort+"&originPort="+originPort;
                GB_show('FCL Rates Comparison Grid',url,document.body.offsetHeight-50,document.body.offsetWidth-100);
            }
            function showRateGrid(route, path){
                var origin = "";
                var destination = "";
                var selectedList = "";
                var checkBoxes = document.getElementsByName("originDestination");
                var destinationPort = document.fclQuotesPopupForm.portofDischarge.value;
                var originPort = "";
                if(trim(document.fclQuotesPopupForm.isTerminal.value)!=""){
                    originPort = document.fclQuotesPopupForm.isTerminal.value;
                }
                for(i=0; i<checkBoxes.length; i++) {
                    if(checkBoxes[i].checked) {
                        if(selectedList == "") {
                            selectedList = checkBoxes[i].value;
                        }else {
                            selectedList = selectedList +","+ checkBoxes[i].value;
                        }
                    }
                }
                if(selectedList == "") {
                    alert("Please Select atleast One");return false;
                }
                if("Origin" == route) {
                    origin = document.fclQuotesPopupForm.isTerminal.value;
                    destination = selectedList;
                }else {
                    destination = document.fclQuotesPopupForm.portofDischarge.value;
                    origin = selectedList;
                }
                var haz = "N";
                if(document.fclQuotesPopupForm.hazmat[0].checked){
                    haz="Y";
                }
                var region = document.getElementsByName("region");
                var selectedRegion = "";
                for(i=0; i<region.length; i++) {
                    if(region[i].checked){
                        selectedRegion = selectedRegion!=""?selectedRegion+', '+region[i].id:region[i].id;
                    }
                }
                var imsChecked = "";
                url = path+'/rateGrid.do?action='+route+'&origin='+origin+
                    "&destination="+destination+"&commodity="+document.fclQuotesPopupForm.commcode.value+
                    '&hazardous='+haz+"&region="+selectedRegion+"&ratesFrom=quickRates&imsChecked="+imsChecked+"&destinationPort="+destinationPort+"&originPort="+originPort;
                GB_show('FCL Rates Comparison Grid',url,document.body.offsetHeight-20,document.body.offsetWidth-100);
            }
            function closeOriginDestinationList() {
                if(null != document.getElementById("originAndDestinationDiv")) {
                    document.body.removeChild(document.getElementById("originAndDestinationDiv"));
                }
                closePopUp();
            }
            function checkShowAllCity() {
                if(document.getElementById("showAllCity") && document.getElementById("showAllCity").checked
                    && (trim(document.fclQuotesPopupForm.portofDischarge.value) == "" || trim(document.fclQuotesPopupForm.isTerminal.value)=="")) {
                    alertNew("PLEASE SELECT DESTINATION PORT AND ORIGIN");
                    document.getElementById("showAllCity").checked = false;
                    return;
                }
            }
            function closeDocList() {
                document.body.removeChild(document.getElementById("attachListDiv"));
                var cover = document.getElementById("cover");
                cover.style.display = "none";
            }
            function checkLock(fileNumber,path,moduleId){
                var lockString='<%=loadString%>';
                var userId='<%=usuerId%>';
                if(lockString!=""){
                    window.parent.changeChilds(path,fileNumber,moduleId);
                }else{
                    ProcessInfoBC.cheackFileINDB(fileNumber,userId,{callback:function(data){
                            if(data!=null && data!=""){
                                if(data == 'sameUser'){
                                    alert("File "+fileNumber+ " is already opened in another window");
                                    return;
                                }else{
                                    alert(fileNumber+" This record is being used by "+data);
                                    window.parent.changeChilds(path);
                                }
                            }else{
                                window.parent.changeChilds(path,fileNumber,moduleId);
                            }
                        },async:false});
                }

	
            }
            function getTemp(){
                var path = "";
                if(document.getElementById('destinationCity').checked){
                    path="&nonRated="
                        +"no"+"&radio=city&origin="+document.fclQuotesPopupForm.isTerminal.value;
                }else{
                    path="&nonRated="
                        +"no"+"&radio=country&origin="+document.fclQuotesPopupForm.isTerminal.value;
                }
                appendEncodeUrl(path);
            }
            function enableQuickRates(){
                document.getElementById('quickRates').disabled = false;
            }
            function getTemp1(){
                document.getElementById('quickRates').disabled = false;
                var path = "";
                if(document.getElementById('originCountry').checked){
                    path="&nonRated="
                        +"no"+"&radio=city&destination="+document.fclQuotesPopupForm.portofDischarge.value;
                }else{
                    path="&nonRated="
                        +"no"+"&radio=country&destination="+document.fclQuotesPopupForm.portofDischarge.value;
                }
                appendEncodeUrl(path);
            }
        </script>

        <link  href="<%=path %>/css/cal/skins/aqua/theme.css" rel="stylesheet" type="text/css" media="all"/>
        <%@include file="../includes/resources.jsp" %>
    </head>

    <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
        <p class="progressBarHeader" style="width: 100%;padding-left:45px;"><b>Processing......Please Wait</b></p>

        <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </form>
    </div>
    <body class="whitebackgrnd"  >

        <div id="cover" ></div>
        <div id="AlertBox" class="alert">
            <p class="alertHeader"><b>Alert</b></p>
            <p id="innerText" class="containerForAlert">

            </p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="OK"
                       onclick="document.getElementById('AlertBox').style.display='none';
                           grayOut(false,'');">
            </form>
        </div>
        <html:form action="/searchquotation?accessMode=${param.accessMode}" name="searchQuotationform" styleId="searchQuotationform"
                   type="com.gp.cvst.logisoft.struts.form.SearchQuotationForm" scope="request">
            <html:hidden property="buttonValue"/>
            <html:hidden property="index"/>
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
                <tr>
                    <td>
                        <%if(displayId==null){ %>
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0"  class="tableBorderNew">
                            <tr class="tableHeadingNew">
                                <td>&nbsp;<br></td>
                                    <%--    <td align="right"><input type="button" value="New Quote" onclick="addquote()" style="font-style:normal;font-family:cursive;font-weight:500;"/></td>--%>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="2" >
                                        <tr class="textlabelsBold">
                                            <td width="7%">File No</td>
                                            <td valign="top">
                                                <html:text property="fileNumber" size="15" styleClass="textlabelsBoldForTextBox" onkeypress="if(event.keyCode==13){searchquotationEnterKey()}" value="${searchQuotationForm.fileNumber}"/>
                                            </td>
                                            <td  width="7%"> Origin</td>
                                            <td><input name="pol" id="pol" size="15"   maxlength="12" class="textlabelsBoldForTextBox" value="${searchQuotationForm.pol}"/>
                                                <input name="pol_check" id="pol_check_id"  type="hidden">
                                                <div id="pol_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                <c:choose>
                                                    <c:when test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("pol","pol_choices","","pol_check",
                                                            "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=6&importFlag=true&isDojo=false&countryflag=false","");
                                                        </script>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("pol","pol_choices","","pol_check",
                                                            "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=6&isDojo=false&countryflag=false","");
                                                        </script>
                                                    </c:otherwise>
                                                </c:choose>
                                            </td>
                                            <td  width="7%">From Date<br></td>
                                            <td>
                                                <div class="foat-left">
                                                    <c:choose>
                                                        <c:when test="${empty searchQuotationForm.quotestartdate}">
                                                            <html:text property="quotestartdate" onchange="return validateDate(this)" value="<%=fromDate%>"  styleId="txtcalbb" size="14" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:text property="quotestartdate" value="${searchQuotationForm.quotestartdate}"
                                                                       styleId="txtcalbb" size="15" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calbb" class="calendar-img"
                                                         onmousedown="insertDateFromCalendar(this.id,0);" />
                                                    <c:choose>  
                                                         <c:when test="${searchQuotationForm.fromSailDateCheck == 'on'}">  
                                                            <input type="checkbox" name="fromSailDateCheck" id="fromSailDateCheck" onclick="seachByFromSailDate()" onmouseover="tooltip.show('<strong>Search Using Sail Date</strong>',null,event);" onmouseout="tooltip.hide();" checked/>  
                                                         </c:when>  
                                                         <c:otherwise>  
                                                            <input type="checkbox" name="fromSailDateCheck" id="fromSailDateCheck" onclick="seachByFromSailDate()" onmouseover="tooltip.show('<strong>Search Using Sail Date</strong>',null,event);" onmouseout="tooltip.hide();" />  
                                                         </c:otherwise>  
                                                     </c:choose>
                                                </div>
                                            </td>
                                            <td>To Date<br></td>
                                            <td>
                                                <div class="float-left">
                                                    <c:choose>
                                                        <c:when test="${empty searchQuotationForm.toDate}">
                                                            <html:text property="toDate" value="<%=toDate%>" styleId="txtcalToyy" size="13" onchange="return validateDate(this)" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:text property="toDate" value="${searchQuotationForm.toDate}" styleId="txtcalToyy" size="13" styleClass="textlabelsBoldForTextBox float-left"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calToyy" class="calendar-img"
                                                         onmousedown="insertDateFromCalendar(this.id,0);"  />
                                                </div>
                                            </td>
                                        </tr>

                                        <tr class="textlabelsBold">
                                            <td>Client
                                                <c:choose>
                                                    <c:when test="${searchQuotationForm.clientCheckbox=='on'}">
                                                        <input type="checkbox" name="clientCheckbox" id="clientCheckbox" onclick="getClient()" onmouseover="tooltip.show('<strong>Disable dojo</strong>',null,event);" onmouseout="tooltip.hide();" checked>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" name="clientCheckbox" id="clientCheckbox" onclick="getClient()" onmouseover="tooltip.show('<strong>Disable dojo</strong>',null,event);" onmouseout="tooltip.hide();" >
                                                    </c:otherwise>
                                                </c:choose>
                                                <br></td>
                                            <td>
                                                <input name="client" id="client"  size="15"  class="textlabelsBoldForTextBox" value="${searchQuotationForm.client}"/>
                                                <input name="client_check" id="client_check"   type="hidden"
                                                       value="${searchQuotationForm.client}" Class="textlabelsBoldForTextBox"/>
                                                <input name="clientNumber" id="clientNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="client_choices" style="display: none" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("client","client_choices","clientNumber","",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=5","");
                                                </script>
                                                <br></td>
                                            <td width="7%">POL<br></td>
                                            <td><input name="plor" id="plor" size="15"      maxlength="12" class="textlabelsBoldForTextBox" value="${searchQuotationForm.plor}"/>
                                                <input name="plor_check" id="plor_check_id"  type="hidden">
                                                <div id="plor_choices"  style="display: none;width: 5px;" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("plor","plor_choices","","plor_check",
                                                    "<%=path%>/actions/getUnlocationCode.jsp?tabName=QUOTE&from=4&isDojo=false","");
                                                </script>
                                                <br></td>
                                            <td>Issuing Terminal<br></td><td>
                                            <c:choose>
                                                    <c:when test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                <input type="text" name="issuingTerminal"  id="issuingTerminal" size="18" class="textlabelsBoldForTextBox" value="${searchQuotationForm.issuingTerminal}"/>
                                                <div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("issuingTerminal","issuingTerminal_choices","","",
                                                    "<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&importFlag=true&isDojo=false","");
                                                </script>
                                            </c:when>
                                            <c:otherwise>
                                                <input type="text" name="issuingTerminal"  id="issuingTerminal" size="18" class="textlabelsBoldForTextBox" value="${searchQuotationForm.issuingTerminal}"/>
                                                <div id="issuingTerminal_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("issuingTerminal","issuingTerminal_choices","","",
                                                    "<%=path%>/actions/getTerminalName.jsp?tabName=QUOTE&importFlag=false&isDojo=false","");
                                                </script>
                                            </c:otherwise>
                                        </c:choose>
                                                <br></td>
                                            <td> Container No<br></td>
                                            <td ><html:text property="container" styleId="txtcal1" size="17" styleClass="textlabelsBoldForTextBox"  onkeypress="if(event.keyCode==13){searchquotationEnterKey()}" value="${searchQuotationForm.container}"/>
                                                <br></td>

                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Shipper<br></td>
                                            <td valign="top">
                                                <input name="shipper" id="shipper" size="15" class="textlabelsBoldForTextBox" value="${searchQuotationForm.shipper}"/>
                                                <input name="shipper_check" id="shipper_check"   type="hidden"
                                                       value="${searchQuotationForm.shipper}" Class="textlabelsBoldForTextBox"/>
                                                <input name="shipperNumber" id="shipperNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="shipper_choices" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("shipper","shipper_choices","shipperNumber","shipper_check",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=6",
                                                    "");
                                                </script>
                                                <br></td>
                                            <td width="7%">POD<br></td>
                                            <td><input name="plod" id="plod" size="15"    maxlength="12" value="${searchQuotationForm.plod}" class="textlabelsBoldForTextBox"/>
                                                <input name="plod_check" id="plod_check_id"  type="hidden">
                                                <div id="plod_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("plod","plod_choices","","plod_check",
                                                    "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=9&isDojo=false","");
                                                </script>

                                                <br></td>
                                            <td>Origin Region<br></td>
                                            <td>
                                                <html:select property="originRegion" style="width:115px"  styleClass="dropdown_accounting"  onchange="" value="${searchQuotationForm.originRegion}">
                                                    <html:option value="0">Select One</html:option>
                                                    <html:optionsCollection name="regions"/>
                                                </html:select>
                                                <br></td>
                                            <td>SSL<br></td>
                                            <td>
                                                <input type="text" Class="textlabelsBoldForTextBox" name="sslDescription" id="sslDescription"  size="22" value="${searchQuotationForm.sslDescription}"/>
                                                <input id="sslname_check" type="hidden" value="${searchQuotationForm.carrier}" />
                                                <div id="sslname_choices" style="display: none" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocompleteWithFormClear("sslDescription","sslname_choices","","sslname_check",
                                                    "<%=path%>/actions/tradingPartner.jsp?tabName=QUOTE&from=1","focusSettingForSSl();","");
                                                </script>
                                            </td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Forwarder
                                                <input type="hidden" style="display: none;" name="sslcode" id="sslcode"/>

                                                <br></td>
                                            <td valign="top">
                                                <input name="forwarder" id="forwarder"  size="15" class="textlabelsBoldForTextBox" value="${searchQuotationForm.forwarder}"/>
                                                <input name="forwarder_check" id="forwarder_check"   type="hidden"
                                                       value="${searchQuotationForm.forwarder}" Class="textlabelsBoldForTextBox"/>
                                                <input name="forwarderNumber" id="forwarderNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="forwarder_choices" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("forwarder","forwarder_choices","forwarderNumber","forwarder_check",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=7",
                                                    "");
                                                </script>
                                                <br></td>
                                            <td>Destination<br></td>
                                            <td><input name="pod" id="pod" size="15"   maxlength="12" class="textlabelsBoldForTextBox" value="${searchQuotationForm.pod}"/>
                                                <input name="pod_check" id="pod_check_id"  type="hidden">
                                                <div id="pod_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <c:choose>
                                                    <c:when test="${loginuser.role.roleDesc == 'AGENT' || loginuser.role.roleDesc == 'agent'}">
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("pod","pod_choices","","pod_check",
                                                            "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=USERAGENT&isDojo=false","");
                                                        </script>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <script type="text/javascript">
                                                            initAutocompleteWithFormClear("pod","pod_choices","","pod_check",
                                                            "<%=path%>/actions/getUnlocationCodeDesc.jsp?tabName=QUOTE&from=1&isDojo=false","");
                                                        </script>
                                                    </c:otherwise>
                                                </c:choose>
                                                <br></td>
                                            <td>Destination Region<br></td>
                                            <td>
                                                <html:select property="destinationRegion" style="width:115px"  styleClass="dropdown_accounting"  value="${searchQuotationForm.destinationRegion}">
                                                    <html:option value="0">Select One</html:option>
                                                    <html:optionsCollection name="regions"/>
                                                </html:select>
                                                <br></td>
                                            <td>Created By<br></td>
                                            <td>
                                                <input type="text" name="quoteBy"  id="quoteBy"   style="width:110px;" class="textlabelsBoldForTextBox" value="${searchQuotationForm.quoteBy}"/>
                                                <c:choose>
                                                    <c:when test="${searchQuotationForm.loginCheck=='on'}">
                                                        <input type="checkbox" id="loginCheck" name="loginCheck" onclick="getLoginName('<%=loginName%>')" checked/>Me
                                                    </c:when>
                                                    <c:otherwise>
                                                        <input type="checkbox" id="loginCheck" name="loginCheck" onclick="getLoginName('<%=loginName%>')"/>Me
                                                    </c:otherwise>
                                                </c:choose>
                                                <div id="quoteBy_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("quoteBy","quoteBy_choices","","",
                                                    "<%=path%>/actions/getUserDetails.jsp?tabName=SEARCH_FILE&from=0&isDojo=false","");
                                                </script>
                                                <br></td>
                                        </tr>
                                        <tr class="textlabelsBold">
                                            <td>Consignee<br></td>
                                            <td><input  name="conginee" id="conginee" class="textlabelsBoldForTextBox"  styleId="txtcal1" size="15" value="${searchQuotationForm.conginee}"/>
                                                <input name="conginee_check" id="conginee_check"   type="hidden"
                                                       value="${searchQuotationForm.conginee}" Class="textlabelsBoldForTextBox"/>
                                                <input name="congineeNumber" id="congineeNumber"   type="hidden" Class="textlabelsBoldForTextBox"/>
                                                <div id="conginee_choices" style="display: none"
                                                     class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("conginee","conginee_choices","congineeNumber","conginee_check",
                                                    "<%=path%>/actions/getAccountDetails.jsp?tabName=QUOTE&from=8",
                                                    "");
                                                </script>
                                                <br></td>
                                            <td>Filter By<br></td>
                                            <td><html:select property="filerBy" styleClass="dropdown_accounting" value="${searchQuotationForm.filerBy}">
                                                    <html:option value="All">All</html:option>
                                                    <html:option value="Quotation">Quotation</html:option>
                                                    <html:option value="Booking">Booking</html:option>
                                                    <html:option value="FclBL">FclBL</html:option>                                                    
                                                    <html:option value="DNR">Doc's Not Received</html:option>
                                                    <html:option value="UMF">Un Manifested</html:option>
                                                    <html:option value="MF">Manifested</html:option>
                                                    <html:option value="MNM">Manifested No Master</html:option>
                                                    <html:option value="SSL">Master Not Received</html:option>
                                                    <html:option value="faeNotApplied">FAE Not Applied</html:option>
                                                    <c:choose>
                                                        <c:when test="${not empty importNavigation}">
                                                            <html:option value="IR">Imp Release</html:option>
                                                            <html:option value="NR">No Release</html:option>
                                                            <html:option value="DR">Doc Release</html:option>
                                                            <html:option value="PR">Pmt Release</html:option>
                                                            <html:option value="OP">Over Paid</html:option>
                                                            <html:option value="Closed">Closed</html:option>
                                                            <html:option value="Audited">Audited</html:option>
                                                            <html:option value="Voided">Voided</html:option>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:option value="P"> Project File</html:option>
                                                        </c:otherwise>
                                                    </c:choose>

                                                </html:select>
                                                <br></td>
                                            <td>SSL Booking #<br></td>
                                            <td><html:text property="ssBkgNo" size="18" 
                                                       styleClass="textlabelsBoldForTextBox"  onkeypress="if(event.keyCode==13){searchquotationEnterKey()}" value="${searchQuotationForm.ssBkgNo}"></html:text>
                                                    <br></td>
                                                <td>Booked By</td>
                                                <td>
                                                    <input type="text" name="bookedBy"  id="bookedBy"    style="width:110px;" class="textlabelsBoldForTextBox" value="${searchQuotationForm.bookedBy}"/>
                                                    <c:choose>
                                                        <c:when test="${searchQuotationForm.loginCheck1=='on'}">
                                                            <input type="checkbox" id="loginCheck1" name="loginCheck1" onclick="getLoginName1('<%=loginName%>')" checked/>Me
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="checkbox" id="loginCheck1" name="loginCheck1" onclick="getLoginName1('<%=loginName%>')"/>Me
                                                        </c:otherwise>
                                                    </c:choose>
                                                <div id="bookedBy_choices"  style="display: none;width: 5px;"  class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initOPSAutocomplete("bookedBy","bookedBy_choices","","",
                                                    "<%=path%>/actions/getUserDetails.jsp?tabName=SEARCH_FILE&from=1&isDojo=false","");
                                                </script>

                                            </td>
                                        </tr>
                                        <tr  class="textlabelsBold"><td>Limit</td>
                                        <td><html:select property="limit" styleClass="dropdown_accounting" value="${searchQuotationForm.limit}">
                                                    <html:option value="250"></html:option>
                                                    <html:optionsCollection name="limitList"/>
                                                </html:select>
                                            </td>
                                            <!--                                            <td>Show VoidBL</td><td>-->
                                            <!--                                                <html:select property="showVoidBL" styleClass="dropdown_accounting" >-->
                                            <!--                                                    <html:option value="N">No</html:option>-->
                                            <!--                                                    <html:option value="Y">YES </html:option>-->
                                                <!--                                                </html:select>-->
                                                <!--                                            </td>-->

                                                <td>Master BL</td>
                                                <td> <input type="text" name="masterBL"  id="bookedBy"  style="width:100px;"  class="textlabelsBoldForTextBox" onkeypress="if(event.keyCode==13){searchquotationEnterKey()}" value="${searchQuotationForm.masterBL}"/></td>
                                                <td>Inbond Number</td>
                                                <td> <input type="text" name="inbondNumber"  id="inbondNumber"   class="textlabelsBoldForTextBox" size="18" onkeypress="if(event.keyCode==13){searchquotationEnterKey()}" value="${searchQuotationForm.inbondNumber}"/></td>
                                                <td>Sort By</td><td>
                                                <html:select property="sortByDate" style="width:115px; " styleClass="dropdown_accounting" value="${searchQuotationForm.sortByDate}">
                                                    <html:option value="S">SELECT</html:option>
                                                    <html:option value="C">Container Cut off</html:option>
                                                    <html:option value="D">Doc Cut Off</html:option>
                                                    <html:option value="E">ETD</html:option>
                                                    <c:if test="${not empty importNavigation }">
                                                        <html:option value="ETA">ETA</html:option>
                                                    </c:if>
                                                </html:select>
                                            </td> </tr>
                                        <tr class="textlabelsBold">
                                            <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                <td>AMS</td><td><html:text property="ams" styleClass="textlabelsBoldForTextBox" value="${searchQuotationForm.ams}"/></td>
                                                <td>Sub-House</td><td><html:text property="subHouse" styleClass="textlabelsBoldForTextBox" value="${searchQuotationForm.subHouse}"/></td>

                                            </c:if>
                                            <c:if test="${empty sessionScope.importNavigation }">
                                                <td>AES ITN</td>
                                                <td> <input type="text" name="aesItn"  id="aesItn"  style="width:100px;"  class="textlabelsBoldForTextBox" 
                                                            onkeypress="if(event.keyCode==13){searchquotationEnterKey()}" value="${searchQuotationForm.aesItn}"/></td>
                                            </c:if>
                                        </tr>

                                        <tr>
                                            <td valign="top" colspan="8"  align="center" style="padding-top:10px;">
                                                <c:if test="${param.accessMode == '1'}">
                                                    <input type="button" value="Quick Rates" id="quickRates" onclick="showRatesPopUp()" class="buttonStyleNew" />
                                                    <c:if test="${sessionScope.quoteAccessMode == '1'}">
                                                        <input type="button" value="New Quote" onclick="addquote('quote')" class="buttonStyleNew"/>
                                                    </c:if>
                                                    <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                        <c:if test="${sessionScope.bookingAccessMode == '1'}">
                                                            <input type="button" value="New Booking"  onclick="addquote('booking')" class="buttonStyleNew"/>
                                                        </c:if>
                                                        <c:if test="${sessionScope.blAccessMode == '1'}">
                                                            <input type="button" value="New Arrival Notice" onclick="addquote('fclBl')" class="buttonStyleNew"/>
                                                        </c:if>
                                                    </c:if>
                                                    <input type="button" value="Search" onclick="searchquotation()" class="buttonStyleNew"/>
                                                    <input type="button" value="Reset" onclick="resetForm()" class="buttonStyleNew"/>
                                                </c:if>
                                                <%--<input type="button" value="Show All" onclick="showall()" class="buttonStyleNew"/> --%>
                                            </td>
                                        </tr>
                                    </table>
                                    <%}%>
                                </td>
                            </tr>
                            <tr style="height:60%"></tr>
                        </table>
                    </td>

                <tr>
                    <td align="top">
                        <%if(displayId!=null){ %>
                        <table width="100%" border="0"  cellpadding="0" cellspacing="0" class="tableBorderNew ">
                            <tr class="tableHeadingNew">
                                <td class="fileno-text1">
                                    <c:if test="${not empty searchQuotationForm.fileNumber}">
                                        <b class="fileno-text1">File Number-><c:out value="${searchQuotationForm.fileNumber}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.pol}">
                                        <b class="fileno-text1">Origin-><c:out value="${searchQuotationForm.pol}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.plor}">
                                        <b class="fileno-text1">POL-><c:out value="${searchQuotationForm.plor}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.plod}">
                                        <b class="fileno-text1">POD-><c:out value="${searchQuotationForm.plod}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.pod}">
                                        <b class="fileno-text1">Destination-><c:out value="${searchQuotationForm.pod}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.carrier}">
                                        <b class="fileno-text1">SSL-> <c:out value="${searchQuotationForm.carrier}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.forwarder}">
                                        <b class="fileno-text1">Forwarder-><c:out value="${searchQuotationForm.forwarder}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.conginee}">
                                        <b class="fileno-text1">Consignee-><c:out value="${searchQuotationForm.conginee}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.rampCity}">
                                        <b class="fileno-text1">Ramp City->
                                            <c:out value="${searchQuotationForm.rampCity}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.issuingTerminal}">
                                        <b class="fileno-text1">Issuing Terminal->
                                            <c:out value="${searchQuotationForm.issuingTerminal}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.client}">
                                        <b class="fileno-text1">Client-><c:out value="${searchQuotationForm.client}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.container}">
                                        <b>Container-><c:out value="${searchQuotationForm.container}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.quoteBy}">
                                        <b class="fileno-text1">Created By-><c:out value="${searchQuotationForm.quoteBy}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.bookedBy}">
                                        <b class="fileno-text1">Booked By-><c:out value="${searchQuotationForm.bookedBy}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.quotestartdate}">
                                        <b class="fileno-text1"> Start Date->
                                            <c:out value="${searchQuotationForm.quotestartdate}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.toDate}">
                                        <b class="fileno-text1"> To Date->
                                            <c:out value="${searchQuotationForm.toDate}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.originRegion && searchQuotationForm.originRegion!='0'}">
                                        <b class="fileno-text1">Origin Region->
                                            <c:out value="${searchQuotationForm.originRegion}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.destinationRegion && searchQuotationForm.destinationRegion!='0'}">
                                        <b class="fileno-text1">Destination Region->
                                            <c:out value="${searchQuotationForm.destinationRegion}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.ssBkgNo && searchQuotationForm.ssBkgNo!='0'}">
                                        <b class="fileno-text1">SSL Booking #->
                                            <c:out value="${searchQuotationForm.ssBkgNo}"></c:out></b>,
                                        </c:if>
                                        <c:if test="${not empty searchQuotationForm.limit && searchQuotationForm.limit!=''}">
                                        <b class="fileno-text1">Limit-><c:out value="${searchQuotationForm.limit}"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='C'}">
                                        <b class="fileno-text1">Sort By-><c:out value="Container Cut off"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='D'}">
                                        <b class="fileno-text1">Sort By-><c:out value="Doc Cut Off"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='DNR'}">
                                        <b class="fileno-text1">Sort By-><c:out value="Doc's Not Received"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='E'}">
                                        <b class="fileno-text1">Sort By-><c:out value="ETD"></c:out></b>,
                                    </c:if>
                                    <c:if test="${not empty searchQuotationForm.sortByDate && searchQuotationForm.sortByDate=='ETA'}">
                                        <b class="fileno-text1">Sort By-><c:out value="ETA"></c:out></b>,
                                    </c:if>
                                </td>
                                <td>
                                    <c:if test="${param.accessMode == '1'}">
                                        <span>
                                            <input type="button" value="Quick Rates" id="quickRates" onclick="showRatesPopUp()" class="buttonStyleNew" />
                                            <input type="button" value="Search" onclick="refereshPage()" class="buttonStyleNew" />
                                            <c:if test="${sessionScope.quoteAccessMode == '1'}">
                                                <input type="button" value="New Quote" onclick="addquote('quote')" class="buttonStyleNew"/>
                                            </c:if>
                                            <c:if test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                <c:if test="${sessionScope.bookingAccessMode == '1'}">
                                                    <input type="button" value="New Booking"  style="width: 80px"  onclick="addquote('booking')" class="buttonStyleNew"/>
                                                </c:if>
                                                <c:if test="${sessionScope.blAccessMode == '1'}">
                                                    <input type="button" value="New Arrival Notice" style="width: 110px" onclick="addquote('fclBl')" class="buttonStyleNew"/>
                                                </c:if>
                                            </c:if>
                                        </span>
                                        <%-- <span style="float: right;">

                                         </span>--%>
                                    </c:if>
                                </td>
                                <td style="float: right">
                                    <b class="fileno-text" style="float:right;">File Search:&nbsp;&nbsp;&nbsp;
                                        <input type="text" id="fileNumberSearch" size="8" class="textlabelsBoldForTextBox" onkeypress="if(event.keyCode==13){searchByFileNumber()}"/>
                                        <img src="<%=path%>/img/icons/magnifier.png" border="0" onclick="searchByFileNumber()"/>
                                        <html:hidden property="fileNumber"/>
                                        <html:hidden property="limit"/>
                                        <html:hidden property="filerBy"/>
                                        <html:hidden property="sortByDate"/>
                                    </b>
                                </td>
                            </tr>
                            <%}%>

                            <tr align="top">
                                <td colspan="3" align="top">
                                    <% int i=0;  %>
                                    <display:table name="<%=displayId%>" id="fileNumberList" pagesize="${searchQuotationForm.limit}"
                                                   class="displaytagstyleNew" sort="list"  >
                                        <display:setProperty name="paging.banner.some_items_found">
                                            <span class="pagebanner">
                                                <font color="blue">{0}</font> Search File details displayed,For more code click on page numbers.
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
                                        <display:setProperty name="basic.msg.empty_list">
                                            <span class="pagebanner">

                                            </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.placement" value="bottom" />
                                        <display:setProperty name="paging.banner.item_name" value="Quotation"/>
                                        <display:setProperty name="paging.banner.items_name" value="Files"/>
                                        <%
                                                String toolTipHeight = "";
                                                if(i==0 || i==1){
                                                        toolTipHeight = "20";
                                                }else{
                                                        toolTipHeight = "20";
                                                }
                                            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
                                            LogFileEdiDAO logFileEdiDAO = new LogFileEdiDAO();
                                                String quotid=null,bookingId=null,fclBl=null,searchDate="",
                                                displayQuoteId="",displayBKId="",displayFCLId="",converted="",color="",corrected="",tooltipValue="";
                                                // folder ID:---
                                                //Strign to diaply tol TIP
                                                String orgTerminal="",orgTer="",destination="",dest="",pol="",polName="",pod="",podName="",status="",status1="";
                                                String user="",textColor="",doorOrigin="",doorDestination="";
                                                String streamLine="",strm="",clientName="",client="",rampCity="",rmpCity="",ssBkgNo="",displaycolor=null;
                                                String issueTerm="",bookingComplete="";
                                                String issue="",fileNumber="",manifest="",ratesNonRates="",hazmat="",prefixFile="",bookedBy="",blClosed="",blAudited="",
                                                docReceived="",blVoid="",dockReceipt="",ediStatus="",trackingStatus="",doorOriginForToolTip="",doorDestinationForToolTip="";boolean drNumber = false;
                                                String importRelease ="", itn="", itnStatus="",masterstatus = "",bookingAesStatus = "",trailerNo="",etaDate="";
                                                Date bolDate = null;
                                                long dateDiff;
                                                Integer aesStatus=0;
                                                  
                                      if(displayId!=null && !displayId.isEmpty()){
                                                        FileNumberForQuotaionBLBooking fileNumberForQuotaionBLBooking=(FileNumberForQuotaionBLBooking)displayId.get(i);
                                                  
                                                request.setAttribute("docsNotReceivedFlag",fileNumberForQuotaionBLBooking.getDocsNotReceivedFlag());
                                                if(fileNumberForQuotaionBLBooking.getDoorOrigin()!=null){
                                                        doorOrigin=fileNumberForQuotaionBLBooking.getDoorOrigin().replace("'","");
                                                }
                                                if(null != fileNumberForQuotaionBLBooking.getAesStatus()){
                                                    itnStatus = fileNumberForQuotaionBLBooking.getAesStatus();
                                                }
                                                if(null == itnStatus || itnStatus.equals("")){
                                                    bookingAesStatus = fileNumberForQuotaionBLBooking.getBookingAesStatus();
                                                    if(null == bookingAesStatus || bookingAesStatus.equals("")){
                                                        aesStatus = fileNumberForQuotaionBLBooking.getAesCount();
                                                    }
                                                }
                                                if(null != fileNumberForQuotaionBLBooking.getDocumentStatus()){
                                                    masterstatus = fileNumberForQuotaionBLBooking.getDocumentStatus();
                                                }

                                                if(fileNumberForQuotaionBLBooking.getDoorDestination()!=null){
                                                        doorDestination=fileNumberForQuotaionBLBooking.getDoorDestination().replace("'","");
                                                }
                                                if(fileNumberForQuotaionBLBooking.getDestination_port()!=null){
                                                    destination = fileNumberForQuotaionBLBooking.getDestination_port();
                                                    if(!destination.equals("")){
                                                      dest = stringFormatter.getBreketValue(destination);
                                                    }
                                                }
                                                if(fileNumberForQuotaionBLBooking.getManifest()!=null){
                                                      manifest=fileNumberForQuotaionBLBooking.getManifest();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getBlAudit()!=null){
                                                      blAudited=fileNumberForQuotaionBLBooking.getBlAudit();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getTrailerNo()!=null){
                                                      trailerNo=fileNumberForQuotaionBLBooking.getTrailerNo();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getBlClosed()!=null && !fileNumberForQuotaionBLBooking.getBlClosed().equals("")){
                                                      blClosed=fileNumberForQuotaionBLBooking.getBlClosed();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getOrigin_terminal()!=null){
                                                   orgTerminal = fileNumberForQuotaionBLBooking.getOrigin_terminal();
                                                   if(!orgTerminal.equals("")){
                                                      orgTer = stringFormatter.getBreketValue(orgTerminal);
                                                   }//if
                                                }
                                                    blVoid=fileNumberForQuotaionBLBooking.getBlvoid();
                                                if(fileNumberForQuotaionBLBooking.getDoorOrigin()!=null && !fileNumberForQuotaionBLBooking.getDoorOrigin().equals("")){
                                                    StringBuilder doorMove=new StringBuilder();
                                                    int index1 = orgTerminal.indexOf("/");
                                                    String origin="";
                                                    if(-1 != index1){
                                                        origin = orgTerminal.substring(0,index1);
                                                    }else{
                                                        origin=orgTerminal;
                                                    }
                                                    if(-1 != orgTerminal.lastIndexOf("(") && -1 != orgTerminal.lastIndexOf(")")){
                                                        origin = origin + orgTerminal.substring(orgTerminal.lastIndexOf("("),orgTerminal.lastIndexOf(")")+1);
                                                        }
                                                        doorMove.append("Origin=");
                                                        doorMove.append(origin);
                                                        doorMove.append("\n");
                                                        doorMove.append("Door Origin=");
                                                        String doorOrg="";
                                                        int index = doorOrigin.indexOf("/");
                                                        if(index!=-1){
                                                           doorOrg=doorOrigin.substring(0,index);
                                                        }else{
                                                           doorOrg=doorOrigin;
                                                        }
                                                        doorMove.append(doorOrg);
                                                        doorOriginForToolTip=doorMove.toString();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getDoorDestination()!=null && !fileNumberForQuotaionBLBooking.getDoorDestination().equals("")){
                                                    StringBuilder doorMove=new StringBuilder();
                                                    int index1 = destination.indexOf("/");
                                                    String destn="";
                                                    if(-1 != index1){
                                                        destn = destination.substring(0,index1);
                                                    }else{
                                                        destn=destination;
                                                    }
                                                    if(-1 != destination.lastIndexOf("(") && -1 != destination.lastIndexOf(")")){
                                                        destn = destn + destination.substring(destination.lastIndexOf("("),destination.lastIndexOf(")")+1);
                                                        }
                                                        doorMove.append("Destination=");
                                                        doorMove.append(destn);
                                                        doorMove.append("\n");
                                                        doorMove.append("Door Destination=");
                                                        String doorDestn="";
                                                        int index = doorDestination.indexOf("/");
                                                        if(index!=-1){
                                                           doorDestn=doorDestination.substring(0,index);
                                                        }else{
                                                           doorDestn=doorDestination;
                                                        }
                                                        doorMove.append(doorDestn);
                                                        doorDestinationForToolTip=doorMove.toString();
                                                }

                                                if(fileNumberForQuotaionBLBooking.getHazmat()!=null){
                                                    hazmat=fileNumberForQuotaionBLBooking.getHazmat();
                                                }else{
                                                    hazmat="";
                                                }
                                                    
                                                if(fileNumberForQuotaionBLBooking.getRatesNonRates()!=null){
                                                   ratesNonRates=fileNumberForQuotaionBLBooking.getRatesNonRates();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getImportRelease()!=null){
                                                   importRelease=fileNumberForQuotaionBLBooking.getImportRelease();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getBookingComplete()!=null && fileNumberForQuotaionBLBooking.getBookingComplete().equalsIgnoreCase("Y")){
                                                bookingComplete="Y";
                                        }
                                                if(fileNumberForQuotaionBLBooking.getPod()!=null){
                                                        podName = fileNumberForQuotaionBLBooking.getPod();
                                                        if(!podName.equals("")){
                                                          pod = stringFormatter.getBreketValue(podName);
                                                          //pod=StringUtils.abbreviate(podName,5);
                                                        }
                                                }
                                                if(fileNumberForQuotaionBLBooking.getPol()!=null){
                                                        polName = fileNumberForQuotaionBLBooking.getPol();
                                                        if(!polName.equals("")){
                                                          pol = stringFormatter.getBreketValue(polName);
                                                          //pol=StringUtils.abbreviate(polName,5);
                                                        }
                                                }
                                                if(fileNumberForQuotaionBLBooking.getIssueTerminal()!=null){
                                                        issueTerm=fileNumberForQuotaionBLBooking.getIssueTerminal();
                                                        issue=issueTerm.indexOf("-") > -1 ?issueTerm.substring(issueTerm.indexOf("-")+1,issueTerm.length()):issueTerm;
                                                }
                                                if(fileNumberForQuotaionBLBooking.getUser()!=null){
                                                        user=fileNumberForQuotaionBLBooking.getUser().toUpperCase();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getBookedBy()!=null){
                                                    bookedBy=fileNumberForQuotaionBLBooking.getBookedBy().toUpperCase();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getFclBlStatus()!=null){
                                                        status1=fileNumberForQuotaionBLBooking.getFclBlStatus()!=null?fileNumberForQuotaionBLBooking.getFclBlStatus():"";
                                                        status1 = !status1.equalsIgnoreCase("null")?status1:"";
                                                    String[]temp=fileNumberForQuotaionBLBooking.getFclBlStatus().split(",");
                                                        String value="";
			 
                                                        for(int k=0;k<temp.length;k++){
                                                           if(temp[k].equals("I")){
                                                              value+="I=Intra"+"<p>";
                                                           }else if(temp[k].equals("E")){
                                                             value=value+"E=Ready To EDI"+"<p>";
                                                           }else if(temp[k].contains("G")){
                                                             value=value+"G=GT NEXUS"+"<p>";
                                                           }else if(temp[k].equals("A")){
                                                             value=value+"A=BL Audited"+"<p>";
                                                           }else if(temp[k].equals("RM")){
                                                              value=value+"RM=Received Master"+"<p>";
                                                           }else if(temp[k].equals("CL")){
                                                              value=value+"CL=BL Closed"+"<p>";
                                                           }else if(temp[k].equals("V")){
                                                              value=value+"V=BL Voided"+"<p>";
                                                           }else if(temp[k].contains("C")){
                                                              value=value+"C=Container Cut Off"+"<p>";
                                                           }else if(temp[k].contains("D")){
                                                              value=value+"D=Doc Cut Off"+"<p>";
                                                           }else if(temp[k].contains("S")){
                                                              value=value+"S=Vessel Sailing Date"+"<p>";
                                                           }else if(temp[k].contains("U")){
                                                              value=value+"U=Number of Units"+"<p>";
                                                           }else if(temp[k].contains("NR")){
                                                              value=value+"NR=Non Rated"+"<p>";
                                                           }else if(temp[k].contains("P")){
                                                              value=value+"P=Project Files"+"<p>";
                                                           }
                                                           status=value;
                                                        }
                                                        status1 =( null != status1)?status1.replaceAll("null",""):status1;
                                                }
                                                if(null !=manifest && !manifest.equals("")){
                                                        status+="M=ManiFest"+"<p>";
                                                        status1+=manifest+",";
                                                        if(fileNumberForQuotaionBLBooking.getCorrectedBL()){
                                                                status+="CorrectedBL";
                                                                status1+="C,";
                                                                //corrected="CC";
                                                        }
                                                        if(fileNumberForQuotaionBLBooking.getCorrectionsPresent()!=null){
                                                            corrected="CC";
                                                        }
                                                        status1 =( null != status1)?status1.replaceAll("null",""):status1;
                                                }
                                                if(fileNumberForQuotaionBLBooking.getSsBkgNo()!=null){
                                                         ssBkgNo=fileNumberForQuotaionBLBooking.getSsBkgNo();
                                                }
                                                if(fileNumberForQuotaionBLBooking.getDocReceived()!=null){
                                                        docReceived=fileNumberForQuotaionBLBooking.getDocReceived();
                                                }
                                                   
                                                if(docReceived.equals("")){
                                                        docReceived="N";
                                                }
                                                if(fileNumberForQuotaionBLBooking.getRampCity()!=null){
                                                        rampCity=fileNumberForQuotaionBLBooking.getRampCity();
                                                        rmpCity=StringUtils.abbreviate(rampCity,16);
                                                }
                                                if(fileNumberForQuotaionBLBooking.getFileNo()!=null){
                                                        fileNumber=fileNumberForQuotaionBLBooking.getFileNo();
                                                        prefixFile=fileNumberPrefix+fileNumber;
                                                        dockReceipt = "04"+fileNumber;
                                                        trackingStatus =fileNumberForQuotaionBLBooking.getTrackingStatus();
                                                }

                                                if(fileNumberForQuotaionBLBooking.get997Success()!=null && !"".equals(fileNumberForQuotaionBLBooking.get997Success())){
                                                    ediStatus ="997Success";
                                                }else if(fileNumberForQuotaionBLBooking.get304Success()!=null){
                                                    ediStatus =fileNumberForQuotaionBLBooking.get304Success();
                                                }else{
                                                    ediStatus =fileNumberForQuotaionBLBooking.get304Failure();
                                                }

                                                if(fileNumberForQuotaionBLBooking.getCarrier()!=null){
                                                        streamLine = fileNumberForQuotaionBLBooking.getCarrier();
                                                        if(null != streamLine){
                                                                streamLine=streamLine.replaceAll("'","\\\\'").replaceAll("\"","&quot;");
                                                        }
                                                        strm=StringUtils.abbreviate(streamLine,20);
                                                }
                                                if(fileNumberForQuotaionBLBooking.getClient()!=null){
                                                        clientName = fileNumberForQuotaionBLBooking.getClient();
                                                        client=StringUtils.abbreviate(clientName,20);
                                                }
                                                if(fileNumberForQuotaionBLBooking.getDisplayColor()!=null){
                                                        displaycolor=fileNumberForQuotaionBLBooking.getDisplayColor();
                                                        fileNumberForQuotaionBLBooking.setDisplayColor(null);
                                                }
                                                if(fileNumberForQuotaionBLBooking.getFileDate()!=null && !fileNumberForQuotaionBLBooking.getFileDate().equals("")){
                                                        searchDate=dateFormat.format(fileNumberForQuotaionBLBooking.getFileDate());
			
                                                }
                                                    if(fileNumberForQuotaionBLBooking.getEtaDate()!=null && !fileNumberForQuotaionBLBooking.getEtaDate().equals("")){
                                                        etaDate=dateFormat.format(fileNumberForQuotaionBLBooking.getEtaDate());
			
                                                }
                                                if(fileNumberForQuotaionBLBooking.getBolDate()!=null && !fileNumberForQuotaionBLBooking.getBolDate().equals("")){
                                                        bolDate=fileNumberForQuotaionBLBooking.getBolDate();

                                                }
                                                if(fileNumberForQuotaionBLBooking.getQuotId()!=null &&
                                                !fileNumberForQuotaionBLBooking.getQuotId().equals("")){
                                                        quotid=fileNumberForQuotaionBLBooking.getQuotId().toString();
                                                        displayQuoteId="quotes";
                                                }
                                                if(fileNumberForQuotaionBLBooking.getBookingId()!=null &&
                                                !fileNumberForQuotaionBLBooking.getBookingId().equals("")){
                                                        bookingId=fileNumberForQuotaionBLBooking.getBookingId().toString();
                                                        displayBKId="booking";
                                                }
                                                if(fileNumberForQuotaionBLBooking.getFclBlId()!=null &&
                                                !fileNumberForQuotaionBLBooking.getFclBlId().equals("")){
                                                        fclBl=fileNumberForQuotaionBLBooking.getFclBlId().toString();
                                                        displayFCLId="bl";
                                                }
                                                        if(!displayQuoteId.equals("") && !displayBKId.equals("") && !displayFCLId.equals("")
                                                        || (!displayQuoteId.equals("") && !displayFCLId.equals(""))){
                                                            //-- when Quotes===>Booking==>>FCL BL conversion OR Qutoes==>>BL Conversion
                                                                converted="FclBl";
                                                        }
                                                        // when Quotes===>Booking  conversion
                                                        else if(!displayQuoteId.equals("") && !displayBKId.equals("")){
                                                                converted="Booking";
                                                        }
                                                        // when Booking==>>FCL BL conversion// have a boubt need to clear
                                                        else if(displayQuoteId.equals("") && !displayBKId.equals("")&& !displayFCLId.equals("")){
                                                                converted="BookingConvertTOBL";
                                                        }
                                                        // when No conversion independent FCL BL
                                                        else if(displayQuoteId.equals("") && displayBKId.equals("")&& !displayFCLId.equals("")){
                                                                converted="NotConvertedBL";
                                                        }
                                                        // when No conversion independent Booking
                                                        else if(displayQuoteId.equals("") && !displayBKId.equals("")&& displayFCLId.equals("")){
                                                                converted="NotConvertedBooking";
                                                        }
                                                        // when No conversion independent Quotes
                                                        else if(!displayQuoteId.equals("") && displayBKId.equals("") && displayFCLId.equals("")){
                                                                converted="Quote";
                                                        }
                                                }
                                                link=editPath+"?eachRowId="+i+"&fileNo="+fileNumber;
                                        %>

                                        <display:column title="Q">
                                            <%
                                            String load = "load";
                                            if(!converted.equals("") && converted.equals("Quote") && ratesNonRates.equals("R")){
                                            %>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong>Rated</strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/lightBlue2.gif" border="0" style="size:100px; "/>
                                            </span>
                                            <%
                                            }else if(!converted.equals("") && converted.equals("Quote") && ratesNonRates.equals("N")){
                                            %>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong>Non-rated</strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/black.gif" border="0" style="size:100px; "/>
                                            </span>
                                            <%} %>
                                        </display:column>

                                        <display:column title="BK">
                                            <%
                                            if(!converted.equals("") && converted.equals("Booking") || converted.equals("NotConvertedBooking")){

                                                    color ="lightBlue2.gif";//QuoteIcon.JPG
                                                    if(converted.equals("NotConvertedBooking")){
                                                            color="lightBlue2.gif";
                                                            tooltipValue="Rated In Process";
                                                    }
                                                    if(!bookingComplete.equals("") && bookingComplete.equalsIgnoreCase("Y")){
                                                            color="darkGreenDot.gif";
                                                            tooltipValue="Booking Complete";
                                                    }
                                                    if(ratesNonRates.equals("N")){
                                                            color="black.gif";
                                                            tooltipValue="Non Rated";
                                                    }
                                                    if(tooltipValue.equals("")){
                                                            tooltipValue="Rated In Process";
                                                    }
                                            //yellow.jpg
                                            %>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=tooltipValue.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);" onmouseout="tooltip.hide();">
                                                <img src="<%=path%>/img/icons/<%=color%>" border="0"/>
                                            </span>
                                            <%
}%>
                                        </display:column>

                                        <display:column title="BL" >
                                            <%if(!converted.equals("") && converted.equals("FclBl") ||  converted.equals("BookingConvertTOBL")
                                             || converted.equals("NotConvertedBL")){
                                                            color ="lightBlue2.gif";//lightBlue22.gif
                                                            if(converted.equals("FclBl")){
                                                                    color="lightBlue2.gif";
                                                                    tooltipValue="BL In Process";
                                                            }
                                                            if(!manifest.equals("")&& manifest.equalsIgnoreCase("M")){
                                                                    color="darkGreenDot.gif";
                                                                    tooltipValue="Manifested";
                                                                    if("Y".equalsIgnoreCase(blClosed)){
                                                                        tooltipValue="Manifested,Closed";
                                                                    }
                                                                    if("Y".equalsIgnoreCase(blAudited)){
                                                                        tooltipValue="Manifested,Closed,Audited";
                                                                    }
                                                            }
                                                            if(!corrected.equals("")&& corrected.equalsIgnoreCase("CC")){
                                                                    color="reddot1.gif";
                                                                    tooltipValue="Corrected";
                                                            }
                                                            if((status1.contains("S")==false)&& !manifest.equalsIgnoreCase("M")){
                                                                    color="yellow.gif";
                                                                    tooltipValue="Sailing Date Past";
                                                            }
                                                            if(null != blVoid && !blVoid.equals("") && blVoid.equals("Y")){
                                                                    color="cross-circle.png";
                                                                    tooltipValue="Void BL";
                                                            }
                                                             if(!"M".equalsIgnoreCase(manifest)&&(status1.contains("S")==true)){
                                                                    Date currentDate=new Date();
                                                                    if(bolDate != null && !bolDate.equals("")){
                                                                        long diff = currentDate.getTime() - bolDate.getTime();
                                                                        dateDiff = (diff/ (1000 * 60 * 60 * 24));
                                                                        if(dateDiff>15){
                                                                            color="orange_dot.png";
                                                                            tooltipValue="BL not manifested";
                                                                        }
                                                                    }
                                                              }
                                            %>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=tooltipValue.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/<%=color%>" border="0" />
                                            </span>
                                            <%
                                            }
                                            %>
                                        </display:column>
                                        <display:column title="HZ">
                                            <%if(hazmat.equals("H")){ %>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong>Hazmat</strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/danger..png" border="0" style="width: 12px "/>
                                            </span>
                                            <%} %>
                                        </display:column>
                                        <display:column title="EDI">
                                            <%if(null != ediStatus && ediStatus.trim().equals("997Success")){ %>
                                            <span class="hotspot"  style="cursor: pointer;" onclick="ediTrack('<%=dockReceipt%>')">
                                                <img src="<%=path%>/img/icons/arrow_green.png" border="0" style="width: 12px "/>
                                            </span>
                                            <%}else if(null != ediStatus && ediStatus.trim().equals("success")){%>
                                            <span class="hotspot"  style="cursor: pointer;" onclick="ediTrack('<%=dockReceipt%>')">
                                                <img src="<%=path%>/img/icons/yellow_arrow.png" border="0" style="width: 12px "/>
                                            </span>
                                            <%}else if(null != ediStatus && ediStatus.trim().equals("failure")){%>
                                            <span class="hotspot"  style="cursor: pointer;" onclick="ediTrack('<%=dockReceipt%>')">
                                                <img src="<%=path%>/img/icons/arrow_red.png" border="0" style="width: 12px "/>
                                            </span>
                                            <%}%>
                                        </display:column>
                                        <display:column title="FileNo" sortable="true"  sortProperty="fileNo">
                                            <% if(!converted.equals("") && converted.equals("Quote")){
													if(displaycolor!=null){%>
                                            <span class="linkSpan" id="quoteCopied" style="font-weight: bold;color:red;background:#00CCFF"
                                                  onclick="checkLock('<%=fileNumber%>','quoteId=<%=quotid%>','QUOTE');"><%=prefixFile%></span>
                                            <%}else{%>
                                            <span class="linkSpan" style="color:black;" onclick="checkLock('<%=fileNumber%>','quoteId=<%=quotid%>','QUOTE');"><%=prefixFile%></span>
                                            <%}}%>

                                            <%if(!converted.equals("") && converted.equals("Booking") || converted.equals("NotConvertedBooking")){
													if(displaycolor!=null){	%>
                                            <span class="linkSpan" id="converted" style="font-weight: bold;color:red;background:#00CCFF"
                                                  onclick="checkLock('<%=fileNumber%>','bookingId=<%=bookingId%>&quoteId=<%=quotid%>','BOOKING');"><%=prefixFile%></span>
                                            <%}else{%>
                                            <span class="linkSpan" style="color:black;"
                                                  onclick="checkLock('<%=fileNumber%>','bookingId=<%=bookingId%>&quoteId=<%=quotid%>','BOOKING')"><%=prefixFile%></span>
                                            <%}}%>

                                            <%
                                            if(!converted.equals("") && converted.equals("FclBl") ||
                                            converted.equals("BookingConvertTOBL")|| converted.equals("NotConvertedBL")){
                                                if(displaycolor!=null){%>
                                            <span class="linkSpan" id="converted" style="font-weight: bold;color:red;background:#00CCFF"
                                                  onclick="checkLock('<%=fileNumber%>','blId=<%=fclBl%>&bookingId=<%=bookingId%>&quoteId=<%=quotid%>&fileNumber=<%=fileNumber%>','FCLBL')"><%=prefixFile%></span>
                                            <%}else{%>
                                            <span class="linkSpan" style="color:black;"
                                                  onclick="checkLock('<%=fileNumber%>','blId=<%=fclBl%>&bookingId=<%=bookingId%>&quoteId=<%=quotid%>&fileNumber=<%=fileNumber%>','FCLBL')"><%=prefixFile%></span>
                                            <%}}%>
                                        </display:column>
                                        <display:column  title="Status"  style="width:85px;" sortProperty="fclBlStatus">
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=status.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=status1%></span>
                                        </display:column>
                                        <c:choose>
                                            <c:when test="${sessionScope.importNavigation!=null and not empty sessionScope.importNavigation }">
                                                <display:column  sortable="true"  title="Rel" >
                                                    <%
                                                        if(null != importRelease && importRelease.trim().equals("YY")){ %>
                                                    <img src="<%=path%>/img/icons/green_check.png" border="0" class="cursorAuto"/>
                                                    <%}else if(null != importRelease && importRelease.trim().equals("YN")) {%>
                                                    <font color="red" style="font-weight: bold">  DR</font>
                                                    <%}else if(null != importRelease && importRelease.trim().equals("NY")) {%>
                                                    <font color="red" style="font-weight: bold">  PR</font>
                                                    <% }%>
                                                </display:column>
                                            </c:when>
                                            <c:otherwise>
                                                <display:column  sortable="true"  title="Doc" >
                                                    <c:choose>
                                                        <c:when test="${docsNotReceivedFlag}">
                                                            <font color="red" style="font-weight: bold;"><%=docReceived%></font>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <%=docReceived%>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <%if(null != masterstatus && masterstatus.trim().equalsIgnoreCase("Approved")) {%>
                                                    ,<font color="green" style="font-weight: bold"> A</font>
                                                    <%}else if(null != masterstatus && masterstatus.trim().equalsIgnoreCase("Disputed")) {%>
                                                    ,<font color="red" style="font-weight: bold"> D</font>
                                                    <% }%>

                                                </display:column>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:choose>
                                            <c:when test="${not empty importNavigation }">
                                                <display:column  sortable="true"  title="Container#" ><%=trailerNo%></display:column>
                                            </c:when>
                                            <c:otherwise>
                                                <display:column  sortable="true"  title="SSL Booking#" ><%=ssBkgNo%></display:column>
                                            </c:otherwise>
                                        </c:choose>

                                        <display:column  title="StartDate"  sortable="true" sortProperty="fileDate"><%=searchDate %></display:column>
                                        <display:column  title="Origin" sortable="true">
                                            <%if(null != doorOriginForToolTip && !doorOriginForToolTip.equalsIgnoreCase("")){ %>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=doorOriginForToolTip.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n","<br>")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:red;font-weight: bold;"><%=orgTer%></span>
                                            <%}else{%>
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=orgTerminal.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=orgTer%></span>
                                            <%}%>
                                        </display:column>
                                        <display:column  title="POL" style="width:10px;" sortable="true">
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=polName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=pol%></span>
                                        </display:column>
                                        <display:column  title="POD" style="width:10px;" sortable="true">
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=podName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=pod%></span>
                                        </display:column>
                                        <c:choose>
                                            <c:when test="${not empty importNavigation }">
                                                <display:column  title="FD" sortable="true">
                                                    <%if(null != doorDestinationForToolTip && !doorDestinationForToolTip.equalsIgnoreCase("")){ %>
                                                    <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=doorDestinationForToolTip.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n","<br>")%></strong>','<%=toolTipHeight%>',event);"
                                                          onmouseout="tooltip.hide();" style="color:red;font-weight: bold;"><%=dest%></span>
                                                    <%}else{%>
                                                    <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=destination.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                          onmouseout="tooltip.hide();" style="color:black;"><%=dest%></span>
                                                    <%}%>
                                                </display:column>
                                            </c:when>
                                            <c:otherwise>
                                                <display:column  title="Destination" sortable="true">
                                                    <%if(null != doorDestinationForToolTip && !doorDestinationForToolTip.equalsIgnoreCase("")){ %>
                                                    <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=doorDestinationForToolTip.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;").replaceAll("\n","<br>")%></strong>','<%=toolTipHeight%>',event);"
                                                          onmouseout="tooltip.hide();" style="color:red;font-weight: bold;"><%=dest%></span>
                                                    <%}else{%>
                                                    <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=destination.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                          onmouseout="tooltip.hide();" style="color:black;"><%=dest%></span>
                                                    <%}%>
                                                </display:column>
                                            </c:otherwise>
                                        </c:choose>
                                        <c:if test="${not empty importNavigation }">
                                            <display:column  sortable="true"  title="ETA" sortProperty="etaDate"><%=etaDate%></display:column>
                                        </c:if>

                                        <display:column  title="Client"  sortable="true">
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=clientName.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=client %></span>
                                        </display:column>
                                        <display:column  title="SSL" sortable="true">
                                            <span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=streamLine.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                                  onmouseout="tooltip.hide();" style="color:black;"><%=strm%></span>
                                        </display:column>
                                        <display:column title="ISS" sortable="true"><span class="hotspot" onmouseover="tooltip.showSmall('<strong><%=issueTerm.replaceAll("'", "\\\\'").replaceAll("\"", "&quot;")%></strong>','<%=toolTipHeight%>',event);"
                                              onmouseout="tooltip.hide();" style="color:black;"><%=issue%></span></display:column>
                                        <display:column title="TR"  sortable="true">
                                            <%if(null != trackingStatus && !trackingStatus.equalsIgnoreCase("")){ %>
                                            <img src="<%=path%>/img/icons/e_contents_view.gif" border="0" onmouseover="tooltip.showSmall('<strong><%=trackingStatus%></strong>','<%=toolTipHeight%>',event);"
                                                 onmouseout="tooltip.hide();" onclick="return GB_show('Notes','<%=path%>/notes.do?moduleId=File&itemName=100018&moduleRefId='+'<%=fileNumber%>',380,780);" />
                                            <%}%>
                                        </display:column>
                                        <c:if test="${empty importNavigation }">
                                            <display:column title="AES"  sortable="true">
                                                <%if(null != itnStatus && !itnStatus.equalsIgnoreCase("")){ %>
                                                <%if(itnStatus.trim().equalsIgnoreCase("Shipment Added") || itnStatus.trim().equalsIgnoreCase("Shipment Replaced")){ %>
                                                <span class="linkSpan"  style="font-weight: bold;background:#00FF00" onmouseover="tooltip.showSmall('<strong><%=itnStatus.replaceAll("'", "").replaceAll("\"", "")%></strong>','<%=toolTipHeight%>',event);"
                                                      onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                <%}else if(itnStatus.toLowerCase().contains("verify")){%>
                                                <span class="linkSpan"  style="font-weight: bold;background:#00FFFF" onmouseover="tooltip.showSmall('<strong><%=itnStatus.replaceAll("'", "").replaceAll("\"", "")%></strong>','<%=toolTipHeight%>',event);"
                                                      onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                <%}else if(itnStatus.toLowerCase().contains("shipment rejected") || !itnStatus.contains("SUCCESSFULLY PROCESSED")){%>
                                                <span class="linkSpan"  style="font-weight: bold;background:#FF0000" onmouseover="tooltip.showSmall('<strong><%=itnStatus.replaceAll("'", "").replaceAll("\"", "")%></strong>','<%=toolTipHeight%>',event);"
                                                      onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                <%}else{%>
                                                <span class="linkSpan"  style="font-weight: bold;background:yellow" onmouseover="tooltip.showSmall('<strong><%=itnStatus.replaceAll("'", "").replaceAll("\"", "")%></strong>','<%=toolTipHeight%>',event);"
                                                      onmouseout="tooltip.hide();" onclick="return GB_showFullScreen('AES Tracking','<%=path%>/aesHistory.do?fileNumber='+'<%=fileNumber%>');">AES</span>
                                                <%}%>
                                                <%}else if(null != bookingAesStatus && !bookingAesStatus.equalsIgnoreCase("")){%>
                                                <span  style="font-weight: bold;background:#00FF00" onmouseover="tooltip.showSmall('<strong><%=bookingAesStatus.replaceAll("'", "").replaceAll("\"", "")%></strong>','<%=toolTipHeight%>',event);"
                                                       onmouseout="tooltip.hide();">AES</span>
                                                <%}else if(null != aesStatus && aesStatus != 0){%>
                                                <span style="font-weight: bold;background:yellow;cursor: pointer" onmouseover="tooltip.showSmall('<strong>Aes Sent</strong>','<%=toolTipHeight%>',event);"
                                                      onmouseout="tooltip.hide();">AES</span>
                                                <%}%>
                                            </display:column>
                                        </c:if>
                                        <display:column title="Created By"  sortable="true">
                                            <%if(null != user && !user.equalsIgnoreCase("")){ %>
                                            <%=user%>
                                            <%}else if(null != bookedBy && !bookedBy.equalsIgnoreCase("")){%>
                                            <%=bookedBy%>
                                            <%}else{%>
                                            <%=user%>   
                                            <%}%>

                                        </display:column>
                                        <display:column title="booked By"  sortable="true"><%=bookedBy%></display:column>
                                        <%i++;%>
                                    </display:table>
                                </td>
                            </tr>
                        </table>
                    </td></tr>
            </table>
            <br/><br/><br/><br/><br/><br/>
            <script>
                <%-- onLoad="disabled('<%=modify%>')"--%>
            </script>
        </html:form>
        <%
        if(null !=session.getAttribute("autoClick") && session.getAttribute("autoClick").equals("autoClick")){
                session.removeAttribute("autoClick");
        %>
        <script>autoClick();</script>
        <%}%>
        <%
                if(null !=session.getAttribute("QuoteCopied") && session.getAttribute("QuoteCopied").equals("QuoteCopied")){
                        session.removeAttribute("QuoteCopied");
        %>
        <script>QuoteCopied();</script>
        <%}%>
    </html:html>
