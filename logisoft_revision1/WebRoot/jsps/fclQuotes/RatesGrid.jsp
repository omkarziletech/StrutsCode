<%@ page language="java" import="java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%> 
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../fragment/formSerialize.jspf"  %>
<%@include file="../includes/jspVariables.jsp"  %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script type="text/javascript" src="${path}/js/jquery/jquery.qtip.js"></script>
<script type="text/javascript" src="${path}/js/jquery/ajax.js"></script>
<script language="text/javascript" src="${path}/js/common.js"></script>
<script type="text/javascript" src="http://maps.google.com/maps/api/js?sensor=true"></script>
<script language="text/javascript" src="${path}/js/rates.js"></script>
<script type="text/javascript" src="/logisoft/js/jquery/jquery.tooltip.js"></script>
<script type="text/javascript" src="/logisoft/js/jquery/jquery.qtip-1.0.0-rc3.min.js"></script>
<link type="text/css" rel="stylesheet" media="screen" href="/logisoft/css/jquery/jquery.tooltip.css" />
<script type="text/javascript">
        jQuery(document).ready(function() {
            jQuery("[title != '']").not("link").tooltip();
        });
</script>
<style type="text/css">
    .toggle, .toggle_collapse{
        vertical-align: bottom;
        padding-bottom: 1px;
    }
</style>
<body onload="setHeight(); closeParentPreloading();">
    <html:form name="rateGridForm" action="/rateGrid" type="com.logiware.form.RateGridForm" method="post" scope="request" styleId="rateGridForm">
<div id="mainDiv">
    <div id="routeDetails">
        <table >
            <tr align="center" class="textlabels" >
                <c:if test="${routeTitle == 'Both'}">
                    <td>File No: </td><td align="left"><b style="color:red" >${fileNo}</b> </td>
                </c:if>
                <c:if test="${param.bulletRates == 'true'}">
                    <td colspan="6" align="center"  style="font-size:large;font-weight: bolder;color: #FF0000">BULLET RATES</td>
                </c:if>
                </tr>
                <tr  align="center" class="textlabels" >
                <c:choose>
                <c:when test="${routeTitle == 'Both'}">
                    <td align="left">Origin: </td><td class="textlabelsBold">${origin}</td><td align="left">Destination: </td><td align="left" class="textlabelsBold">${destination}</td><td align="left">Commodity: </td>
                    <td align="left" class="textlabelsBold">${commodityCode}</td>
                    <td><font color="red" size="4">${HazardousMessage}</font></td>
                </c:when>
                <c:otherwise>
                    <td>${routeTitle}: </td><td class="textlabelsBold">${routeName}</td><td align="left">Commodity: </td><td align="left" class="textlabelsBold">${commodityCode}</td>
                    <td><font color="red" size="4">${HazardousMessage}</font></td>
                    <c:if test="${!empty region}">
                        <td>Region: </td><td class="textlabelsBold">${region}</td>
                    </c:if>
                </c:otherwise>
                </c:choose>
                <c:if test="${!empty doorOrigin}">
                    <td>${doorLabel}: </td><td class="textlabelsBold">${doorOrigin}<td>
                </c:if>
                <c:if test="${!empty bulletRatesList}">
                <td width="35%" align="right"><b> Bullet Rates</b></td>
                    <td align="right">
                        <html:select property="bulletRates" style="width:220px;" value="${commodityCode}" onchange="loadBulletRates()"
                                     styleId="selectBulletRatess" styleClass="dropdown_accounting">
                            <html:optionsCollection name="bulletRatesList"/>
                        </html:select>
                    </td>
                </c:if>
                </tr>
        </table>
        <table class="textlabels">
            <tr>
                <td align="left" style="width: 30%">
                    <c:choose>
                        <c:when test="${ratesFrom ne 'quickRates' && ratesFrom ne 'multiRates'}">
                            <input type="button" value="Submit" class="buttonStyleNew" onclick="submitRates('${copyQuote}');"/>
                        </c:when>
                        <c:otherwise>
                            <c:if test="${ratesFrom eq 'multiRates'}">
                                <input type="button" value="Submit" class="buttonStyleNew" onclick="submitMultiRates();"/>
                            </c:if>
                        </c:otherwise>
                    </c:choose>                    
                    <IMG onclick="getMode('expand')" border=0 src="${path}/img/icons/up.gif">
                    <INPUT style="VISIBILITY: visible" id="expand" class="buttonStyleNew" onclick="getMode('normal')" value="Expand" type="button"/>
                    <INPUT style="VISIBILITY: hidden" id="collapse" value ="Collapse" class="buttonStyleNew" onclick="getMode('collapse')" type="button"/>
                    <input type="button" value="Cancel" class="buttonStyleNew" onclick="enableQuickRats();"/>&nbsp;
                    <img onclick="showMap()" src="${path}/img/map_1.png" style="vertical-align:top" alt="Map" complete="complete"/>
                </td>
                <td align="right" style="width: 70%">
                    <c:if test="${isImsRates && not empty imsCollapse}">
                        <input type="button" value="Total with Inland" class="buttoncolor" id="totalIms" onclick="totalWithIms(this)" style="VISIBILITY: visible"/>&nbsp;
                    </c:if>
                    <c:if test="${not empty ImsError}">
                        <font color="Red" size="3">${ImsError}</font>
                    </c:if>
                </td>
            </tr>
        </table>
    </div>
    <div id="rateGrid" class="displaytagstyleNew" align="center" style="width:100%;height:300px;overflow:scroll">
        ${collapse}
    </div>
    <div id="imsCollapseDiv" style="display: none">
        ${imsCollapse}
    </div>
    <div id="collapseDiv" style="display: none">
        ${collapse}
    </div>
    <div id="normalDiv" style="display: none">
        ${normal}
    </div>
    <div id="expandDiv" style="display: none">
        ${expand}
    </div>
</div>
<script>
    function selectUnits(val) {
        if(document.getElementById("imsCheck_"+val.split(";")[0]+":"+val.split(";")[1])){
            jQuery(".ims_checkbox").attr("checked", false);
            document.getElementById("imsCheck_"+val.split(";")[0]+":"+val.split(";")[1]).checked = true;
        }else{
            jQuery(".ims_checkbox").attr("checked", false);
        }
        var unit = document.getElementById(val).value;
        var units = unit.split(",",unit.length);
        var unitType = document.getElementsByName("unitType");
        for(i=0; i<unitType.length; i++) {
            unitType[i].checked=false;
            var selected = false;
            for(j=0; j<units.length;j++) {
                if(unitType[i].value==units[j]) {
                    unitType[i].disabled = false;
                    unitType[i].checked = true;
                    selected = true;
                    break;
                }
            }
            if(!selected) {
                unitType[i].checked = false;
                unitType[i].disabled = true;
            }
        }
    }
    function selectUnitsMulti(val) {
//    var unit = document.getElementById(val).value;
//    alert(document.getElementById("key_4").value)
        var unitType = document.getElementsByName("unitType");
            for(i=0; i<unitType.length; i++) {  
                    unitType[i].checked = false; 
                     unitType[i].disabled = false;
            }      
            var selected = false;
        jQuery(".sSLineBox").each(function () {          
        if(jQuery(this).is(":checked")){            
            selected = true;
            var val1 = jQuery(this).val();
            var unit1 = document.getElementById(val1).value;        
            var units1 = unit1.split(",",unit1.length);
            var unitType1 = document.getElementsByName("unitType");
        for(i=0; i<unitType1.length; i++) {  
            for(j=0; j<units1.length;j++) { 
                if(unitType1[i].value == units1[j]) {                     
                    unitType1[i].checked = true;
                    break;
                }                
            }
        }
    }
        });
       if(selected){
      jQuery("#rateGrid").find("input[name='unitType']").each(function(){
         if(!(jQuery(this).is(":checked"))){
            for(i=0; i<unitType.length; i++) {
               if(!(unitType[i].checked)){
                    unitType[i].disabled = true; 
                }
            } 
        }
     });
 }
}
             
    
    function submitMultiRates(){
//        var bulletRates = "${param.bulletRates}";
//        var originalCommodity = "${param.originalCommodity}";
//        var currentCommodity = document.getElementById("selectBulletRatess") ? document.getElementById("selectBulletRatess").value : "";
//        var bulletRatesCheck = (bulletRates === "true" || originalCommodity != currentCommodity);
//        var selected = false;
//        var imsTrucker = "";
//        var imsBuy = "";
//        var imsSell = "";
//        var imsQuoteNo = "";
//        var imsLocation = "";
//        var imsOrigin = "";
//        if(jQuery("input[name='imsQuote']:checked").length>0){
//             var row = jQuery("input[name='imsQuote']:checked").parent();
//             imsTrucker = row.find(".imsTrucker").val();
//             imsBuy = row.find(".imsBuy").val();
//             imsSell = row.find(".imsSell").val();
//             imsQuoteNo = row.find(".imsQuoteNo").val();
//             imsLocation = row.find(".imsLocation").val();
//             imsOrigin = jQuery("input[name='imsQuote']:checked").val();
//        }
            var ssline = [];
            var selectedUnits = [];
            var selectionInsert = [];                
//            var selectionsInsert = [];
            var selected = false;
        jQuery("#rateGrid").find("input[name='sSLine']").each(function(){
           if(jQuery(this).is(":checked")){
                ssline.push(jQuery(this).val());
                var selection = document.getElementById(jQuery(this).val()+"_OLD").innerHTML;
                selectionInsert.push(selection+"&");
//                var selections = selection.split('-;',6);
//                selectionsInsert.push(selections);
                selected = true;
                     
        }
        });        
               
        jQuery("#rateGrid").find("input[name='unitType']").each(function(){
            if(jQuery(this).is(":checked")){
                selectedUnits.push(jQuery(this).val());  
             
            }
        });
        if(!selected){
            alert("Please Select the Rates");
             return false;        
        } else{
//     1       var selection = document.getElementById(ssline+"_OLD").innerHTML;                
//     1       var selections = selection.split('-;',6);
           
            //    1        parent.parent.call();
 
//          parent.parent.document.multiQuotesForm.ssLine=ssline;
//          parent.parent.document.multiQuotesForm.selectionInsert=selectionInsert;
//          parent.parent.document.multiQuotesForm.selectedUnits=selectedUnits;
//          parent.parent.GB_hide();
//          parent.parent.closeOriginDestAndQckRates();
          parent.parent.selectedMenuMulti(ssline, selectionInsert, selectedUnits);
            return false;
        }                
       
    }
    
    function submitRates(copyQuote){
        var bulletRates = "${param.bulletRates}";
        var originalCommodity = "${param.originalCommodity}";
        var currentCommodity = document.getElementById("selectBulletRatess") ? document.getElementById("selectBulletRatess").value : "";
        var bulletRatesCheck = (bulletRates === "true" || originalCommodity != currentCommodity);
        var selected = false;
        var imsTrucker = "";
        var imsBuy = "";
        var imsSell = "";
        var imsQuoteNo = "";
        var imsLocation = "";
        var imsOrigin = "";
        if(jQuery("input[name='imsQuote']:checked").length>0){
             var row = jQuery("input[name='imsQuote']:checked").parent();
             imsTrucker = row.find(".imsTrucker").val();
             imsBuy = row.find(".imsBuy").val();
             imsSell = row.find(".imsSell").val();
             imsQuoteNo = row.find(".imsQuoteNo").val();
             imsLocation = row.find(".imsLocation").val();
             imsOrigin = jQuery("input[name='imsQuote']:checked").val();
        }
        jQuery("#rateGrid").find("input[name='sSLine']").each(function(){           
           if(jQuery(this).is(":checked")){
                var ssline = jQuery(this).val();
                var selectedUnits = [];
                jQuery("#rateGrid").find("input[name='unitType']").each(function(){
                   if(jQuery(this).is(":checked")){
                        selectedUnits.push(jQuery(this).val());
                   }
                });
                var selection = document.getElementById(ssline+"_OLD").innerHTML;
                selected = true;
                var selections = selection.split('-;',6);
                if(parent.parent.document.QuotesForm != null) {
                    parent.parent.document.QuotesForm.isTerminal.value = selections[2];
                    parent.parent.document.QuotesForm.portofDischarge.value = selections[3];
                    parent.parent.document.QuotesForm.localdryage.value = selections[5];
                    parent.parent.document.QuotesForm.imsTrucker.value = imsTrucker;
                    parent.parent.document.QuotesForm.imsBuy.value = imsBuy;
                    parent.parent.document.QuotesForm.imsSell.value = imsSell;
                    parent.parent.document.QuotesForm.imsQuoteNo.value = imsQuoteNo;
                    parent.parent.document.QuotesForm.imsLocation.value = imsLocation;
                    parent.parent.document.QuotesForm.imsOrigin.value = imsOrigin;
                }else {
                    parent.parent.document.searchQuotationform.isTerminal.value = selections[2];
                    parent.parent.document.searchQuotationform.portofDischarge.value = selections[3];
                    parent.parent.document.searchQuotationform.localdryage.value = selections[5];
                    parent.parent.document.searchQuotationform.imsTrucker.value = imsTrucker;
                    parent.parent.document.searchQuotationform.imsBuy.value = imsBuy;
                    parent.parent.document.searchQuotationform.imsSell.value = imsSell;
                    parent.parent.document.searchQuotationform.imsQuoteNo.value = imsQuoteNo;
                    parent.parent.document.searchQuotationform.imsLocation.value = imsLocation;
                    parent.parent.document.searchQuotationform.imsOrigin.value = imsOrigin;
                }
                if(parent.parent.document.getElementById("bulletRates") && bulletRatesCheck){
                    parent.parent.document.getElementById("bulletRates").checked=true;
                }
                parent.parent.closeOriginDestinationList();
                parent.parent.GB_hide();
                parent.parent.call();
                parent.parent.selectedMenu(selections[0]+"//"+selections[1],selections[0]+"//"+selections[1]+"=="+selections[4],selectedUnits,selections[2],selections[3],selections[4],copyQuote);
                return false;
           }
        });
        if(!selected){
            alert("Please Select the Rates");
        }

    }
    function oneSelected(ele){
        if(jQuery(ele).attr("checked")){
            jQuery(".ims_checkbox").attr("checked", false);
            jQuery(ele).attr("checked", true);
        }
    }
    function enableQuickRats(){
	if(parent.document.getElementById('quickRateDestination')){
	    parent.closeLightBox();
	}else{
	    parent.parent.document.getElementById('getRates').disabled = false;
	    parent.parent.GB_hide();
	}
    }
    function totalWithIms(obj){
        if(obj.value == 'Total with Inland'){
            document.getElementById("rateGrid").innerHTML = document.getElementById("imsCollapseDiv").innerHTML;
            obj.value = 'Remove Inland from Total';
        }else{
            document.getElementById("rateGrid").innerHTML = document.getElementById("collapseDiv").innerHTML;
            obj.value = 'Total with Inland';
        }
        sweetTitles.init();
    }
    function getMode(val) {
        if(val == "normal") {
            document.getElementById("rateGrid").innerHTML = document.getElementById("normalDiv").innerHTML;
            document.getElementById("collapse").style.visibility="visible";
            document.getElementById("expand").style.visibility="hidden";
            if(document.getElementById("totalIms")){
                document.getElementById("totalIms").style.visibility="hidden";
            }
        }else if(val == "expand") {
            document.getElementById("rateGrid").innerHTML = document.getElementById("expandDiv").innerHTML;;
            document.getElementById("collapse").style.visibility="visible";
            document.getElementById("expand").style.visibility="visible";
            if(document.getElementById("totalIms")){
                document.getElementById("totalIms").style.visibility="hidden";
            }
        }else if(val == "collapse") {
            document.getElementById("rateGrid").innerHTML = document.getElementById("collapseDiv").innerHTML;;
            document.getElementById("collapse").style.visibility="hidden";
            document.getElementById("expand").style.visibility="visible";
            if(document.getElementById("totalIms")){
                document.getElementById("totalIms").style.visibility="visible";
            }
        }
        sweetTitles.init();
    }
    function showBreakDown(amount,fuel,allRate){
        tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$'+amount+'</a><br><a style=\'color: green\'>CurrentFuel :</a>  <a style=\'color: blue\'>$'+fuel+'</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$'+allRate+'</a></strong>',null,event);
    }
    function showBreakDownWithHaz(amount,fuel,allRate,haz){
        tooltip.showSmall('<strong><a style=\'color: green\'>Break Down</a><br>-----------------------------<br><a style=\'color: green\'>Base Quote</a> : <a style=\'color: blue\'>$'+amount+'</a><br><a style=\'color: green\'>CurrentFuel :</a>  <a style=\'color: blue\'>$'+fuel+'</a><br><a style=\'color: green\'>Hazardous :</a>  <a style=\'color: blue\'>$'+haz+'</a><br>-----------------------------<br>   <a style=\'color: green\'>Total :</a> <a style=\'color: blue\'>$'+allRate+'</a></strong>',null,event);
    }
    function imsToopTip(){
        tooltip.showSmall('<strong>Check here to include Inland rates on quote</strong>',null,event);
    }
    function showMap() {
        //window.open("/logisoft/jsps/fclQuotes/GoogleMap.jsp?cities='${cityList}'", "City", "width=100%, height=100%,resizable=yes,scrollbars=yes,status=yes")
        parent.parent.parent.GB_show("GoogleMap", "/logisoft/jsps/fclQuotes/RatesGridMap.jsp?cities=${cityList}&sessionMapKey=${sessionMapKey}&doorOrigin=${doorOrigin}&zip=${zip}",document.body.offsetHeight+180,document.body.offsetWidth + 100);
    }
    function setHeight() {
        document.getElementById("rateGrid").style.height=document.body.offsetHeight-75;
    }

    function closeParentPreloading(){
        if(window.parent.parent.parent.closePreloading){
                window.parent.parent.parent.closePreloading();
            }else{
                window.parent.parent.closePreloading();
            }
    }

    function loadBulletRates(){
            var url="${url}";
            url += "&commodity="+document.getElementById("selectBulletRatess").value;
            window.location.assign(url);
            if(window.parent.parent.parent.showPreloading){
                window.parent.parent.parent.showPreloading();
            }else{
                window.parent.parent.showPreloading();
            }
    }
    function showAdditionalRates(ele){
        var parent = jQuery(ele).parent().parent();
        parent.find(".additionalRates").slideToggle();
        parent.find(".toggle").hide();
        parent.find(".toggle_collapse").show();
    }

    function hideAdditionalRates(ele){
        var parent = jQuery(ele).parent().parent();
        parent.find(".additionalRates").slideToggle();
        parent.find(".toggle").show();
        parent.find(".toggle_collapse").hide();
    }

    function showToolTip(ele,w,e){
        var comment = jQuery(ele).attr("comment");
        tooltip.show(comment,w,e);
    }
 function JToolTip(selector,width){
    var message=jQuery(selector).attr("comment");
    jQuery(selector).qtip({
        content: message,
        style: {
            width: width,
            tip: {
                corner: 'topRight',
                color: '#8DB7D6'
            },
            border: {
                width: 1,
                radius: 3,
                color: '#8DB7D6'
            }
        },
        position: {
            corner: {
                target: 'bottomRight',
                tooltip: 'topRight'
            }
        },
        show: 'mouseover',
        hide: 'mouseout'
    });
}

jQuery(document).ready(function(){
     if(jQuery.browser.msie){
	 var version = jQuery.browser.version;
	 if(version.indexOf("10.")>-1){
	     jQuery("body").addClass("ie10");
	 }else if(version.indexOf("9.")>-1){
	     jQuery("body").addClass("ie9");
	 }else if(version.indexOf("8.")>-1){
	     jQuery("body").addClass("ie8");
	 }else if(version.indexOf("7.")>-1){
	     jQuery("body").addClass("ie7");
	 }
     }else{
	 jQuery("body").addClass("non-ie");
     }
     jQuery(".more-info").each(function(){
         JToolTip(this,400);
     });
 });
</script>
<script type="text/javascript"
		src="${path}/js/sweet-titles-tooltip/addEvent.js"></script>
	<script type="text/javascript"
		src="${path}/js/sweet-titles-tooltip/sweetTitles.js"></script>
	<script type="text/javascript"
		src="${path}/js/tablesorter/jquery.tablesorter.js"></script>
	<script type="text/javascript"
		src="${path}/js/tablesorter/jquery.tablesorter.pager.js"></script>
        <link rel="stylesheet" type="text/css" href="${path}/css/sweetTitles.css" />
</html:form>
</body>
