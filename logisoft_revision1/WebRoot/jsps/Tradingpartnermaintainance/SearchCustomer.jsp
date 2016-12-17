<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.bc.tradingpartner.*,com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerTemp,java.util.*,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerInfo,com.gp.cong.logisoft.beans.customerBean"%>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String buttonValue="";
String amsg="";
String account1="off";
String acctName ="";
String acctNo ="";
String field="";
String vAddress="";
String vCity="";
String vUnlocCode="";
String lclFlag="";
int i=0;

customerBean customerbean=new customerBean(); //creating object for customer bean
if(session.getAttribute("customerbean")!=null){
   customerbean=(customerBean)session.getAttribute("customerbean");
   if(customerbean.getName() != null){
     acctName = customerbean.getName();
   }
   if(customerbean.getAccountNo() != null){
     acctNo = customerbean.getAccountNo();
   }
}
request.setAttribute("customerbean",customerbean);
request.setAttribute("customer",customer);

String callFrom="";
if(request.getParameter("callFrom")!=null){
       callFrom = request.getParameter("callFrom");
}
if(request.getAttribute("callFrom")!=null){
       callFrom = (String)request.getAttribute("callFrom");
}
if(request.getParameter("field")!=null){
   field = request.getParameter("field");
}
if(request.getAttribute("field")!=null){
  field = (String)request.getAttribute("field");
}
//LCL
if(request.getParameter("vacctname")!=null){
  acctName = request.getParameter("vacctname");
}
if(request.getParameter("vAddress")!=null){
  vAddress = request.getParameter("vAddress");
}
if(request.getParameter("vCity")!=null){
  vCity = request.getParameter("vCity");
}
if(request.getParameter("vUnLocCode")!=null){
  vUnlocCode = request.getParameter("vUnLocCode");
}
if(request.getParameter("lclFlag")!=null){
  lclFlag = request.getParameter("lclFlag");
}
if(session.getAttribute("trade")!=null){
  session.removeAttribute("trade");
}

if(session.getAttribute("amsg")!=null){ //if starts here 4 checkin amsg,if amsg contains data its displayed
        amsg=(String)session.getAttribute("amsg");
        if(amsg!=""){
%>
<script type="text/javascript">
    alert(<%=amsg%>);// used to display the supplied msg of amsg
</script>
<%}}//if ends here

if(request.getAttribute("buttonValue")!=null){
   buttonValue=(String)request.getAttribute("buttonValue");
}

String message="";
List customerList=null;

String modify="";

if(session.getAttribute("message")!=null){ //if starts 4 checking nullness for message
        message=(String)session.getAttribute("message");
} //if ends

if(session.getAttribute("customerList")!=null){//if starts 4 checking nullness for customerList
    customerList=(List)session.getAttribute("customerList");
}  //if ends

if(request.getParameter("modify")!= null){
  modify=(String)request.getParameter("modify");
  session.setAttribute("modifyforcustomer",modify);
}else{
  modify=(String)session.getAttribute("modifyforcustomer");
}

if(request.getParameter("programid")!=null){
  String programId=request.getParameter("programid");
  session.setAttribute("processinfoforcustomer",programId);
}
String type1="mb";

if(session.getAttribute("tradingPartner")!=null){//to retrive & display the record been added
  TradingPartner tradingpartner=(TradingPartner)session.getAttribute("tradingPartner");
  tradingpartner.getAccountno();
}
if(session.getAttribute("tradingPartner")!=null){
  session.removeAttribute("tradingPartner");
}


session.setAttribute("customerList",customerList);
String editPath=path+"/searchCustomer.do";
request.setAttribute("sortByList",dbUtil.getSortByList());
request.setAttribute("limitList", dbUtil.limitList());
%>

<html>
    <head>
        <title>JSP for CustomerForm form</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf"  %>
        <link type="text/css" rel="stylesheet" href="<%=path%>/css/jquery.autocomplete.css"/>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/ajax.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="<%=path%>/js/jquery/jquery-impromptu.js"></script>
        <script type="text/javascript" src="<%=path%>/autocompleter/js/jquery.autocomplete.js"></script>
        <script type="text/javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/common.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript">
            dojo.hostenv.setModulePrefix('utils', 'utils');
            dojo.widget.manager.registerWidgetPackage('utils');
            dojo.require("utils.AutoComplete");
        </script>
        <script type=text/css>
            .down {
            background: url("/logisoft/images/icons/Kn_descending.gif") left no - repeat;
            }
    .up {
    background: url("/logisoft/images/icons/Kn_ascending.gif") left no - repeat;
    }
        </script>
        <script type="text/javascript">

            var newwindow = '';
            function addform() {
                if (!newwindow.closed && newwindow.location){
                    newwindow.location.href = "<%=path%>/jsps/Tradingpartnermaintainance/CustomerCode.jsp";
                }
                else {
                    newwindow=window.open("<%=path%>/jsps/Tradingpartnermaintainance/CustomerCode.jsp","","width=400,height=150");
                    if (!newwindow.opener) newwindow.opener = self;
                }
                if (window.focus) {newwindow.focus()}
                return false;
            }
            function searchform(){
                if((jQuery("#account").val().length !== 0 && jQuery("#account").val().length < 3)
                    ||(jQuery("#name").val().length !== 0 && jQuery("#name").val().length < 3)
                    ||(jQuery("#blueScreenAccount").val().length !== 0 && jQuery("#blueScreenAccount").val().length < 3)
                    ||(jQuery("#searchAddress").val().length !== 0 && jQuery("#searchAddress").val().length < 3)){
                    return;
                }
                if(document.tradingPartnerForm.name.value==''){
                    document.tradingPartnerForm.name.value = document.tradingPartnerForm.ord_name.value;
                }
                if(document.tradingPartnerForm.searchBySubType.value==""){
                    if(document.tradingPartnerForm.name.value=="" && document.tradingPartnerForm.account.value=="" && document.tradingPartnerForm.searchAddress.value=="" && document.tradingPartnerForm.blueScreenAccount.value==""
                        && !(document.getElementById('accountType1').checked) <%--&& !(document.getElementById('accountType2').checked)--%> && !(document.getElementById('accountType3').checked)
                        && !(document.getElementById('accountType4').checked)
            <%--&& !(document.getElementById('accountType5').checked) && !(document.getElementById('accountType6').checked)
            && !(document.getElementById('accountType7').checked)--%> && !(document.getElementById('accountType8').checked) && !(document.getElementById('accountType9').checked)
                            && !(document.getElementById('accountType10').checked) && !(document.getElementById('accountType11').checked)
                            && (document.getElementById('accountType13') && !document.getElementById('accountType13').checked)){
                            alert("Enter Search Criteria");
                            document.tradingPartnerForm.name.value=="";
                            document.tradingPartnerForm.name.focus();
                            return;
                        }
                    }
                    document.tradingPartnerForm.buttonValue.value="SearchMachList";
                    document.tradingPartnerForm.submit();
                }
                function searchform1(){
                    if(document.tradingPartnerForm.accountName.value==""){
                        alert("Please enter the Account Name");
                        return;
                    }
                    document.tradingPartnerForm.buttonValue.value="saveCustomer";
                    document.tradingPartnerForm.submit();
                }
                function viewCustomer(val1){
                    document.tradingPartnerForm.tradingPartnerId.value=val1;
                    document.tradingPartnerForm.buttonValue.value="editCustomer";
                    document.tradingPartnerForm.submit();
                }
                function moreInfoCustomer(val1){
                    document.tradingPartnerForm.tradingPartnerId.value=val1;
                    document.tradingPartnerForm.buttonValue.value="moreInfoCustomer";
                    document.tradingPartnerForm.submit();
                }
                function setAutoComplete(enabled){
                    if(enabled){
                        document.getElementById("autoComplete").style.display="block";
                        document.getElementById("nameTextBox").style.display="none";
                    }else{
                        document.getElementById("autoComplete").style.display="none";
                        document.getElementById("nameTextBox").style.display="block";
                    }
                }
                function showToolTip(enabled){
                    if(enabled){
                        tooltip.show("Checked = AutoComplete Enabled",null,event);
                    }else{
                        tooltip.show("UnChecked = AutoComplete Disabled",null,event);
                    }
                }
                function addNew(acctName,address,city,unLocCode) {
                    var field='<%=field%>';
                    var callFrom = document.getElementById("callFrom").value;
                    if(null!=callFrom && callFrom!="" && callFrom!="null"){
                        //			window.parent.parent.parent.GB_show('Add Customer','<%=path%>/jsps/Tradingpartnermaintainance/AddTradingPartner.jsp?from=layout&master=no&fromField='+field+"&callFrom="+callFrom+"&accountName="+acctName+"&city="+city+"&unlocCode="+unLocCode,350,850);
                        GB_show('Add Customer','<%=path%>/jsps/Tradingpartnermaintainance/AddTradingPartner.jsp?from=layout&master=no&fromField='+field+"&callFrom="+callFrom+"&accountName="+acctName+"&city="+city+"&unlocCode="+unLocCode,350,850);
                    }else{
                        GB_show('Add Customer','<%=path%>/jsps/Tradingpartnermaintainance/AddTradingPartner.jsp?master=no&fromField='+field,350,850);
                    }
                }
                function search(){
                    document.tradingPartnerForm.buttonValue.value="removesession";
                    document.tradingPartnerForm.submit();
                }
                function getUppercase(val){
                    val=val.toUpperCase();
                    document.tradingPartnerForm.name.value=val;
                }
                function getUppercaseAddress(val){
                    val=val.toUpperCase();
                    document.tradingPartnerForm.searchAddress.value=val;
                }
                function getUppercaseBlueScreenAccount(val){
                    val=val.toUpperCase();
                    document.tradingPartnerForm.blueScreenAccount.value=val;
                }
                function getUppercaseAcctNo(val){
                    val=val.toUpperCase();
                    document.tradingPartnerForm.account.value=val;
                }
                function getUppercaseName(val){
                    val=val.toUpperCase();
                    document.tradingPartnerForm.ord_name.value=val;
                }
                function checkDisableOrNot(ev){
                    jQuery.ajaxx({
                        data: {
                            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                            methodName: "getDisabledRecord",
                            param1: ev
                        },
                        success: function (data) {
                            if(jQuery.trim(data) !== ""){
                                jQuery("#disabledMessage").html(data);
                            }
                        }
                    });
                }
                function setDisabledMessage(data){
                    if(null != data && undefined != data){
                        if(null!=data.disabled && undefined != data.disabled && data.disabled=="Y"){
                            var dateTime = data.tempFormattedDate;
                            var date = dateTime.substring(0,dateTime.indexOf(" "));
                            var time = dateTime.substring(dateTime.indexOf(" ")+1,dateTime.length);
                            document.getElementById('disabledMessage').innerHTML = 'This Trading Partner was disabled on '+date+' at '+time;
                        }
                    }
                }
                function onLoad(val){
                    if(val=='consigneeNameBL'||val=='houseConsigneeNameBL'||val=='consigneename'){
                        document.getElementById("accountType4").checked=true;
                    }
                    if(val=='shipperNameBL'||val=='shipperNameMasterBL'||val=='shipperName'){
                        document.getElementById("accountType1").checked=true;
                    }
                    if(val=='notifyPartyNameBL'||val=='houseNotifyPartyNameBL'){
                        document.getElementById("accountType3").checked=true;
                    }
                    //              if(val=='forwardingAgentNameBL'||val=='fowardername'){
                    //                document.getElementById("accountType2").checked=true;
                    //              }
                }
        </script>
    </head>
    <body class="whitebackgrnd" >
        <%@include file="../preloader.jsp"%>
        <div id="cover" style="width: 100% ;height: 1000px;"></div>
        <div id="ConfirmBox" class="alert">
            <p class="alertHeader"><b>Confirmation</b></p>
            <p id="innerText1" class="containerForAlert">

            </p>
            <div style="text-align:right;padding-right:4px;padding-bottom:4px;padding-top:10px;">
                <input type="button"  class="buttonStyleForAlert" value="OK" id="confirmYes"
                       onclick="yes()">
                <input type="button"  class="buttonStyleForAlert" value="Cancel" id="confirmNo"
                       onclick="No()">
            </div>
        </div>
        <c:choose>
            <c:when test="${param.accessMode==1}">
                <c:set var="tpEditMode" value="true" scope="session"/>
            </c:when>
            <c:when test="${param.accessMode==0 || tpEditMode==false}">
                <c:set var="tpEditMode" value="false" scope="session"/>
            </c:when>
            <c:otherwise>
                <c:set var="tpEditMode" value="true" scope="session"/>
            </c:otherwise>
        </c:choose>
        <html:form action="/tradingPartner"  name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
            <%@include file="customerSearchOptions.jsp"%>
            <font color="red" size="4" id="disabledMessage" style="font-weight:bold;"></font>
            <font color="red" size="4" id="allReadyExist"></font>
            <font color="blue" size="4"><%=message%></font>
            <c:choose>
                <c:when test="${empty sessionScope.tradingPartnerSearchList}">
                    <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew" colspan="2"><td>Search Customer</td>
                            <td align="right">
                                <input type="button" class="buttonStyleNew" value="Add New" id="addNewCustomerToggle"
                                       onclick="addNew()"/>
                            </td>
                        </tr>
                        <tr>
                            <td colspan="2">
                                <div id="search_customer_vertical_slide" >
                                    <table width="100%" border="0" cellpadding="0" cellspacing="10">
                                        <tr class="textlabelsBold">
                                            <td>By Name</td>
                                            <td>
                                                <div id="autoComplete">
                                                    <input name="name" Class="textlabelsBoldForTextBox" id="name" value="<%=acctName%>" size="35" onkeyup="getUppercase(this.value)"/>
                                                    <img src="<%=path%>/images/icons/search_filter.png" id="customerSearchEdit" onmouseover="tooltip.show('<strong>Click here to Edit Customer Search options</strong>', null, event);" onmouseout="tooltip.hide();"
                                                         style="vertical-align: middle" onclick="showCustomerSearchOption();"/>
                                                    <input Class="textlabelsBoldForTextBox"  name="name_check" id="name_check" type="hidden" value="<%=acctName%>" />
                                                    <input Class="textlabelsBoldForTextBox"  name="tempAcount" id="tempAcount" type="hidden" value="" />
                                                    <div id="name_choices" style="display: none" class="autocomplete"></div>
                                                </div>
                                                <div id="nameTextBox" style="display: none">
                                                    <input name="ord_name" class="textlabelsBoldForTextBox" id="ord_name" value="<%=acctName%>" size="35" onkeydown ="getUppercaseName(this.value)"/>
                                                </div>
                                            </td>
                                            <td align="left">By Account # </td>
                                            <td align="left">
                                                <input name="account" Class="textlabelsBoldForTextBox" id="account" value="<%=acctNo%>" size="15" onkeydown ="getUppercaseAcctNo(this.value)"/>
                                                <dojo:autoComplete formId="tradingPartnerForm" textboxId="account" action="<%=path%>/actions/getAccountNo.jsp?tabName=SEARCH_CUSTOMER&from=0" />
                                            </td>
                                            <td align="left">By Blue Screen account # </td>
                                            <td align="left">
                                                <input name="blueScreenAccount" Class="textlabelsBoldForTextBox" id="blueScreenAccount" value="" size="15" onkeydown ="getUppercaseBlueScreenAccount(this.value)"/>

                                            </td>
                                            <td  align="right">By Address</td>
                                            <td>
                                                <html:text property="searchAddress" styleClass="textlabelsBoldForTextBox" styleId="searchAddress" size="35" onkeydown ="getUppercaseAddress(this.value)" ></html:text>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td class="textlabelsBold"><input type=checkbox onclick="setAutoComplete(this.checked);"
                                                                              checked="checked"> Enable AutoComplete Feature
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="65%" border="0">
                                        <tr class="textlabelsBold">
                                            <td style="padding-right: 15px;">By Account Type </td>
                                            <td width="0%" style="padding-right: 13px;"><span  onmouseover="tooltip.show('Shipper',null,event);" onmouseout="tooltip.hide();">S</span><html:checkbox property="accountType1" styleId="accountType1" style="vertical-align: middle" name="customerbean" value="S" ></html:checkbox></td>
                                            <%--<td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Forwarder');" onmouseout="tooltip.hide();">F</span><html:checkbox property="accountType2" styleId="accountType2" style="vertical-align: middle" name="customerbean"  /></td>--%>
                                            <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('NVOCC',null,event)" onmouseout="tooltip.hide();">N</span><html:checkbox property="accountType3" styleId="accountType3" style="vertical-align: middle"  name="customerbean" value="N"/></td>
                                            <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Consignee',null,event)" onmouseout="tooltip.hide();">C</span><html:checkbox property="accountType4" styleId="accountType4" style="vertical-align: middle" name="customerbean" value="C"/></td>
                                            <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Import Agent',null,event)" onmouseout="tooltip.hide();">AI</span><html:checkbox property="accountType8" styleId="accountType8"  style="vertical-align: middle" name="customerbean" value="I"/></td>
                                            <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Export Agent',null,event)" onmouseout="tooltip.hide();">AE</span><html:checkbox property="accountType9" styleId="accountType9" style="vertical-align: middle" name="customerbean" value="E"/></td>
                                            <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Vendor',null,event)" onmouseout="tooltip.hide();">V</span><html:checkbox property="accountType10" styleId="accountType10" style="vertical-align: middle" name="customerbean" value="V"/></td>
                                            <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Others',null,event)" onmouseout="tooltip.hide();">O</span><html:checkbox property="accountType11" style="vertical-align: middle" styleId="accountType11" value="O"/></td>
                                            <c:if test="${not empty loginuser && loginuser.role.roleDesc=='Admin'}">
                                                <td width="0%" style="padding-right: 13px;"><span onmouseover="tooltip.show('Company',null,event)" onmouseout="tooltip.hide();">Z</span><html:checkbox property="accountType13" style="vertical-align: middle" styleId="accountType13" value="Z"/></td>
                                            </c:if>
                                            <td>Sub type</td>
                                            <td>
                                                <html:select property="searchBySubType" styleClass="verysmalldropdownStyleForText" style="border:1px solid #c4c5c4">
                                                    <html:optionsCollection name="subTypeList" styleClass="unfixedtextfiledstyle"/>
                                                </html:select>
                                            </td>
                                            <td>Sort By</td>
                                            <td><html:select property="sortBy" value="${tradingPartnerForm.sortBy}" style="border:1px solid #c4c5c4" styleClass="verysmalldropdownStyleForText">
                                                    <html:optionsCollection name="sortByList"/>
                                                </html:select></td>
                                            <td>Limit</td>
                                            <td><html:select property="limit" value="${tradingPartnerForm.limit}" style="border:1px solid #c4c5c4" styleClass="verysmalldropdownStyleForText">
                                                    <html:optionsCollection name="limitList"/>
                                                </html:select>
                                            </td>
                                        </tr>
                                        <tr><table border="0" width="100%">
                                            <tr>
                                                <td width="50%" align="center">
                                                    <input type="button" class="buttonStyleNew" value="Search" id="search" onclick="searchform()"  />
                                            </tr>
                                        </table></tr>
                                    </table>
                                </div>
                            </td>
                        </tr>
                    </table>
                </c:when>
                <c:otherwise>
                    <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew" colspan="2"><td>Search Customer</td>
                            <td align="right">
                                <input type="button" class="buttonStyleNew" value="Search" 	id="addNewCustomerToggle"
                                       onclick="search()"/>
                            </td>
                        </tr>
                        <tr>
                            <td><table width="100%">
                                    <tr class="textlabelsBold"><td>Name</td>
                                        <td>Account No</td>
                                        <td>Blue Screen account</td>
                                        <td>Account Type</td>
                                        <c:if test="${not empty subType}">
                                            <td>Sub Type</td>
                                        </c:if>
                                        <td>Sort By</td>
                                    </tr>
                                    <tr class="textlabelsBold" style="color:blue">
                                        <td><c:out value="${tradingPartnerForm.name}"/></td>
                                        <td><c:out value="${tradingPartnerForm.account}"></c:out></td>
                                        <td><c:out value="${tradingPartnerForm.blueScreenAccount}"></c:out></td>
                                        <td><c:out value="${accountType}"/></td>
                                        <c:if test="${not empty subType}">
                                            <c:choose>
                                                <c:when test="${subType == 'Steamship Line' && not empty sslineNumber}">
                                                    <td><c:out value="${subType}(${sslineNumber})"/></td>
                                                </c:when>
                                                <c:otherwise>
                                                    <td><c:out value="${subType}"/></td>
                                                </c:otherwise>
                                            </c:choose>
                                        </c:if>
                                        <td><c:if test="${tradingPartnerForm.sortBy=='1'}"><c:out value="Account Name"/></c:if>
                                            <c:if test="${tradingPartnerForm.sortBy=='2'}"><c:out value="Account No"/></c:if>
                                            <c:if test="${tradingPartnerForm.sortBy=='6'}"><c:out value="Address"/></c:if></td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                        </table>
                        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew" ><td>Search Results</td></tr>
                            <tr>
                                <td>
                                 <c:choose>
                                    <c:when test= "${fn:length(tradingPartnerSearchList)>0}">
                                        ${fn:length(tradingPartnerSearchList)} Records Found.
                                    </c:when>
                                    <c:otherwise>
                                        No Records Found.
                                    </c:otherwise>
                                </c:choose>
                                <c:if test="${not empty tradingPartnerSearchList}">
                                    <table class="display-table" sort="list">
                                        <thead>
                                            <tr>
                                                <th id="accountName" class="${tradingPartnerForm.sortByValueForTp eq 'accountName' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('accountName')">Customer Name</a>
                                                </th>
                                                <th id="accountNumber" class="${tradingPartnerForm.sortByValueForTp eq 'accountNumber' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('accountNumber')">Acct_no</a>
                                                </th>
                                                <th id="accountType" class="${tradingPartnerForm.sortByValueForTp eq 'accountType' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('accountType')">Account Type</a>
                                                </th>
                                                <th id="subType" class="${tradingPartnerForm.sortByValueForTp eq 'subType' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('subType')">Sub Type</a>
                                                </th>
                                               
                                                <th id="eciAcctNoList" class="${tradingPartnerForm.sortByValueForTp eq 'eciAcctNoList' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('eciAcctNoList')">ECI Shipr/FF#</a>
                                                </th>

                                                <th id="eciAcctNo2List" class="${tradingPartnerForm.sortByValueForTp eq 'eciAcctNo2List' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('eciAcctNo2List')">ECI Cong</a>
                                                </th>
                                                <th id="eciAcctNo3List" class="${tradingPartnerForm.sortByValueForTp eq 'eciAcctNo3List' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('eciAcctNo3List')">ECI ven</a>
                                                </th>

                                                <th id="creditStatus" class="${tradingPartnerForm.sortByValueForTp eq 'creditStatus' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('creditStatus')">Credit Status</a>
                                                </th>
                                                <th id="creditLimit" class="${tradingPartnerForm.sortByValueForTp eq 'creditLimit' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('creditLimit')">Credit limit</a>
                                                </th>
                                                <th>ImpCR</th>
                                                <th id="colleCtor" class="${tradingPartnerForm.sortByValueForTp eq 'colleCtor' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('colleCtor')">Collector</a>
                                                </th>
                                                <th id="address" class="${tradingPartnerForm.sortByValueForTp eq 'address' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('address')">Addr</a>
                                                </th>
                                                <th id="city" class="${tradingPartnerForm.sortByValueForTp eq 'city' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('city')">City</a>
                                                </th>
                                                <th id="state" class="${tradingPartnerForm.sortByValueForTp eq 'state' ? tradingPartnerForm.searchTypeForTp : ''}">
                                                    <a href="javascript:doSort('state')">ST</a>
                                                </th>
                                                    <th>CR</th>
                                                    <th>HD</th>
                                                    <th>IN</th>
                                                    <th>Forward Account</th>
                                                    <th>Mst Acct#</th>
                                                    <th>Actions</th>
                                                </tr>
                                            </thead>
                                            <tbody>
                                                <c:set var="rowIndex" value="0"/>
                                                <c:forEach var="infobean" items="${tradingPartnerSearchList}">
                                                    <c:choose>
                                                        <c:when test="${rowStyle eq 'odd'}">
                                                            <c:set var="rowStyle" value="even"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <c:set var="rowStyle" value="odd"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    <tr class="${rowStyle}">
                                                        <td>
                                                            <span class="hotspot" onmouseover="tooltip.show('<strong>${infobean.accountName}</strong>',null,event);"
                                                                  onmouseout="tooltip.hide();">${fn:substring(infobean.accountName, 0, 30)}</span>

                                                        </td>
                                                        <td>
                                                            ${infobean.accountNumber}  
                                                        </td>
                                                        <td>
                                                            ${infobean.accountType}
                                                            <c:if test="${infobean.disabled == 'Y'}">
                                                                <font color="red" >(Disabled)</font>
                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            ${infobean.subType}
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test = "${roleDuty.manageECIAccountLink eq true}">
                                                                    <c:if test="${not empty infobean.eciAcctNoList}">
                                                                        <c:choose>
                                                                            <c:when test="${infobean.disabled != 'Y'}">
                                                                                <html:select property="eciAccountNo" styleId="eciAccountNo${rowIndex}" value="${infobean.eciAcctNo}"  styleClass="dropdown_accounting unfixedtextfiledstyle" onchange="saveEciShipperAccounting('${rowIndex}','${infobean.accountNumber}');">
                                                                                    <c:forEach var="myMap" items="${infobean.eciAcctNoList}">
                                                                                        <html:option value="${myMap}">${myMap}</html:option>
                                                                                    </c:forEach>
                                                                                </html:select> 
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <html:select property="eciAccountNo" styleId="eciAccountNo${rowIndex}" value="${infobean.eciAcctNo}" disabled="true" styleClass="dropdown_accounting unfixedtextfiledstyle" style="border: 0px;background-color:#CCEBFF;color: #333333;font-size: 10px;" onchange="saveEciShipperAccounting('${rowIndex}','${infobean.accountNumber}');">
                                                                                    <c:forEach var="myMap" items="${infobean.eciAcctNoList}">
                                                                                        <html:option value="${myMap}">${myMap}</html:option>
                                                                                    </c:forEach>
                                                                                </html:select>
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${not empty infobean.eciAcctNoList}">
                                                                        <html:select property="eciAccountNo" styleId="eciAccountNo${rowIndex}" value="${infobean.eciAcctNo}" styleClass="dropdown_accounting unfixedtextfiledstyle" disabled="true" style="border: 0px;background-color:#CCEBFF;color: #333333;font-size: 10px;">

                                                                            <c:forEach var="myMap" items="${infobean.eciAcctNoList}">
                                                                                <html:option value="${myMap}">${myMap}</html:option>
                                                                            </c:forEach> 
                                                                        </html:select>
                                                                    </c:if>

                                                                </c:otherwise>
                                                            </c:choose>

                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test = "${roleDuty.manageECIAccountLink eq true}">
                                                                    <c:if test="${not empty infobean.eciAcctNo2List}">
                                                                        <c:choose>
                                                                            <c:when test= "${infobean.disabled != 'Y'}">
                                                                                <html:select property="eciAccountNo2" styleId="eciAccountNo2${rowIndex}" value="${infobean.eciAcctNo2}" styleClass="dropdown_accounting unfixedtextfiledstyle" onchange="saveEciConsigneeAccounting('${rowIndex}','${infobean.accountNumber}');">
                                                                                    <c:forEach var="myMap" items="${infobean.eciAcctNo2List}">
                                                                                        <html:option value="${myMap}">${myMap}</html:option>
                                                                                    </c:forEach>
                                                                                </html:select>
                                                                            </c:when>
                                                                            <c:otherwise>

                                                                                <html:select property="eciAccountNo2" styleId="eciAccountNo2${rowIndex}" value="${infobean.eciAcctNo2}" disabled="true" styleClass="dropdown_accounting unfixedtextfiledstyle" style="border: 0px;background-color:#CCEBFF;color: #333333;font-size: 10px;" onchange="saveEciConsigneeAccounting('${rowIndex}','${infobean.accountNumber}');">
                                                                                    <c:forEach var="myMap" items="${infobean.eciAcctNo2List}">
                                                                                        <html:option value="${myMap}">${myMap}</html:option>
                                                                                    </c:forEach>
                                                                                </html:select>
                                                                            </c:otherwise>

                                                                        </c:choose>

                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${not empty infobean.eciAcctNo2List}">
                                                                        <html:select property="eciAccountNo2" styleId="eciAccountNo2${rowIndex}" value="${infobean.eciAcctNo2}" styleClass="dropdown_accounting unfixedtextfiledstyle" disabled="true" style="border: 0px;background-color:#CCEBFF;color: #333333;font-size: 10px;">

                                                                            <c:forEach var="myMap" items="${infobean.eciAcctNo2List}">
                                                                                <html:option value="${myMap}">${myMap}</html:option>
                                                                            </c:forEach> 
                                                                        </html:select>
                                                                    </c:if>

                                                                </c:otherwise>
                                                            </c:choose>

                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test = "${roleDuty.manageECIAccountLink eq true}">
                                                                    <c:if test="${not empty infobean.eciAcctNo3List}">
                                                                        <c:choose>
                                                                            <c:when test="${infobean.disabled != 'Y'}">
                                                                                <html:select property="eciAccountNo3" styleId="eciAccountNo3${rowIndex}" value="${infobean.eciAcctNo3}" styleClass="dropdown_accounting unfixedtextfiledstyle" onchange="saveEciVendorAccounting('${rowIndex}','${infobean.accountNumber}');">
                                                                                    <c:forEach var="myMap" items="${infobean.eciAcctNo3List}">
                                                                                        <html:option value="${myMap}">${myMap}</html:option>
                                                                                    </c:forEach>
                                                                                </html:select>
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <html:select property="eciAccountNo3" styleId="eciAccountNo3${rowIndex}" value="${infobean.eciAcctNo3}" disabled="true" styleClass="dropdown_accounting unfixedtextfiledstyle" style="border: 0px;background-color:#CCEBFF;color: #333333;font-size: 10px;" onchange="saveEciVendorAccounting('${rowIndex}','${infobean.accountNumber}');">
                                                                                    <c:forEach var="myMap" items="${infobean.eciAcctNo3List}">
                                                                                        <html:option value="${myMap}">${myMap}</html:option>
                                                                                    </c:forEach>
                                                                                </html:select>
                                                                            </c:otherwise>
                                                                        </c:choose>

                                                                    </c:if>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <c:if test="${not empty infobean.eciAcctNo3List}">
                                                                        <html:select property="eciAccountNo3" styleId="eciAccountNo3${rowIndex}" value="${infobean.eciAcctNo3}" styleClass="dropdown_accounting unfixedtextfiledstyle" disabled="true" style="border: 0px;background-color:#CCEBFF;color: #333333;font-size: 10px;">

                                                                            <c:forEach var="myMap" items="${infobean.eciAcctNo3List}">
                                                                                <html:option value="${myMap}">${myMap}</html:option>
                                                                            </c:forEach> 
                                                                        </html:select>
                                                                    </c:if>

                                                                </c:otherwise>
                                                            </c:choose>

                                                        </td>
                                                        <td>
                                                            ${infobean.creditStatus}
                                                        </td>
                                                        <td>
                                                            ${infobean.creditLimit}
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${null != infobean.impCr && infobean.impCr == 'Y'}">
                                                                    <input type="checkbox" name="impCr" id="impCr${rowIndex}" value="${infobean.impCr}" checked="true" onclick="saveImpCr('${rowIndex}','${infobean.accountNumber}')"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="impCr" id="impCr${rowIndex}" value="${infobean.impCr}"  onclick="saveImpCr('${rowIndex}','${infobean.accountNumber}')"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            ${infobean.colleCtor}
                                                        </td>
                                                        <td>           
                                                            <c:if test="${null != infobean.address && not empty infobean.address }">
                                                                <c:choose>
                                                                    <c:when test="${fn:contains(infobean.address,'/')}">

                                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>${infobean.address}</strong>',null,event);"
                                                                              onmouseout="tooltip.hide();">${fn:replace(fn:substring(infobean.address, 0,15), '/', '')}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>${infobean.address}</strong>',null,event);"
                                                                              onmouseout="tooltip.hide();">${fn:substring(infobean.address , 0,15)}</span> 
                                                                    </c:otherwise>             
                                                                </c:choose>

                                                            </c:if>
                                                        </td>
                                                        <td>
                                                            <c:if test="${null != infobean.city && not empty infobean.city }">
                                                                <c:choose>
                                                                    <c:when test="${fn:contains(infobean.city,'/')}">
                                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>${infobean.city}</strong>',null,event);"
                                                                              onmouseout="tooltip.hide();">${fn:replace(fn:substring(infobean.city, 0 ,15),'/','')}</span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        <span class="hotspot" onmouseover="tooltip.show('<strong>${infobean.city}</strong>',null,event);"
                                                                              onmouseout="tooltip.hide();">${fn:substring(infobean.city,0,15)}</span>
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </c:if>

                                                        </td>
                                                        <td>
                                                            ${infobean.state}
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${null != infobean.credit && infobean.credit == 'Y'}">
                                                                    <input type="checkbox" name="credit" checked="true" onclick="return false;" value="${infobean.credit}"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="credit"  onclick="return false;" value="${infobean.credit}"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${null != infobean.hold && infobean.hold == 'Y'}">
                                                                    <input type="checkbox" name="credit" checked="true" onclick="return false;" value="${infobean.hold}"/>        
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="credit"  onclick="return false;" value="${infobean.hold}"/>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${null != infobean.insurance && infobean.insurance == 'Y'}">
                                                                    <input type="checkbox" name="insurance" checked="true" onclick="return false;" value=" ${infobean.insurance}"/> 
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="insurance" onclick="return false;"  value=" ${infobean.insurance}"/>   
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </td>
                                                        <td>
                                                            <c:set var="readOnlyForwardAccount" value="false"/>
                                                            <c:set var="readOnlyClass" value="textlabelsBoldForTextBox"/>
                                                            <c:if test="${infobean.disabled == 'Y'}">
                                                                <c:set var="readOnlyForwardAccount" value="true"/>
                                                                <c:set var="readOnlyClass" value="BackgrndColorForTextBox"/>
                                                            </c:if>
                                                            <html:text property="forwardAccount" styleId="forwardAccount${rowIndex}" styleClass="${readOnlyClass} uppercase"
                                                                       value="${infobean.forwardAccount}" readonly="${readOnlyForwardAccount}" style="float:left;width:80px;"/>

                                                            <input type="hidden" name="forwardAccountName" id="forwardAccountName${rowIndex}"/>
                                                            <input type="hidden" name="forwardAccountCheck" id="forwardAccountCheck${rowIndex}" value="${infobean.forwardAccount}"/>
                                                            <div id="forwardAccountChoices${rowIndex}" style="display: none;float:left" class="autocomplete"></div>
                                                            <script type="text/javascript">
                                        AjaxAutocompleter("forwardAccount${rowIndex}", "forwardAccountChoices${rowIndex}", "forwardAccountName${rowIndex}", "forwardAccountCheck${rowIndex}",
                                        "${path}/servlet/AutoCompleterServlet?action=Vendor&textFieldId=forwardAccount${rowIndex}&excludeAccountNo=${infobean.accountNumber}", "");
                                                            </script>
                                                            <c:if test="${roleDuty.disableOrEnableTp}">
                                                                <c:set var="enableImg" value="none"/>
                                                                <c:set var="disableImg" value="block"/>
                                                                <c:if test="${infobean.disabled == 'Y'}">
                                                                    <c:set var="enableImg" value="block"/>
                                                                    <c:set var="disableImg" value="none"/>
                                                                </c:if>
                                                                <img id="EnableImg${rowIndex}" title="Click here to Enable" alt="Enable" src="${path}/images/Unlock.png"
                                                                     onclick="enableTradingPartner('${infobean.accountNumber}','${infobean.eciAcctNo}','${infobean.eciAcctNo2}','${infobean.eciAcctNo3}','${infobean.sslineNumber}','${rowIndex}','${infobean.shipperUserName}','${infobean.consigneeUserName}','${infobean.firmsCode}')" style="display: ${enableImg};float:left"/>
                                                                <img id="DisableImg${rowIndex}" title="Click here to Disable" alt="Disable"
                                                                     src="${path}/images/Lock.png" onclick="disableTradingPartner('${infobean.accountNumber}', '${rowIndex}')" style="display: ${disableImg};float:left"/>
                                                            </c:if>
                                                            <c:set var="rowIndex" value="${rowIndex+1}"/>

                                                        </td>
                                                        <td>
                                                            <c:choose>
                                                                <c:when test="${roleDuty.changeMaster eq true}">
                                                                    <c:set var="readOnlymasterAccount" value="false"/>
                                                                    <c:set var="readOnlyClass" value="textlabelsBoldForTextBox"/>
                                                                    <%--  <c:if test="${customertable.disabled == 'Y'}">
                                                                          <c:set var="readOnlymasterAccount" value="true"/>
                                                                          <c:set var="readOnlyClass" value="BackgrndColorForTextBox"/>
                                                                      </c:if> --%>
                                                                    <html:text property="masterAccount" styleId="masterAccount${rowIndex}" styleClass="${readOnlyClass} uppercase"
                                                                               value="${infobean.masterAccount}" readonly="${readOnlymasterAccount}" style="float:left;width:80px;"/>

                                                                    <input type="hidden" name="masterAccountName" id="masterAccountrName${rowIndex}"/>
                                                                    <input type="hidden" name="masterAccountCheck" id="masterAccountCheck${rowIndex}" value="${infobean.masterAccount}"/>
                                                                    <div id="masterAccountChoices${rowIndex}" style="display: none;float:left" class="autocomplete"></div>
                                                                    <script type="text/javascript">
                                                AjaxAutocompleter("masterAccount${rowIndex}", "masterAccountChoices${rowIndex}", "masterAccountrName${rowIndex}", "masterAccountCheck${rowIndex}",
                                                "${path}/servlet/AutoCompleterServlet?action=Master&textFieldId=masterAccount${rowIndex}&excludeAccountNo=${infobean.accountNumber}","SaveMasterAccoount('${rowIndex}','${infobean.accountNumber}')","");
                                                                    </script>
                                                                    <c:set var="rowIndex" value="${rowIndex+1}"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="text" readonly="true"  Class="BackgrndColorForTextBox" value="${infobean.masterAccount}" />
                                                                </c:otherwise>
                                                            </c:choose>

                                                        </td>
                                                        <td>
                                                            <c:if test="${tpEditMode}">
                                                                <img src="<%=path%>/img/icons/edit.gif" border="0" alt="edit" title="edit"
                                                                     onclick="viewCustomer('${infobean.accountNumber}')"/>
                                                            </c:if>
                                                            <img src="<%=path%>/img/icons/container_obj.gif" border="0"  alt="view" title="view"
                                                                 onclick="moreInfoCustomer('${infobean.accountNumber}')">
                                                            <%--<span class="hotspot" onmouseover="tooltip.show('<strong>MOre Info</strong>');"
                                                                                                        onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0"
                                                                                                        onclick="moreInfoCustomer('${customertable.accountNumber}')" /></span>--%>

                                                        </td>
                                                    </tr>
                                                    <c:set var="rowIndex" value="${rowIndex+1}"/>
                                                </c:forEach>
                                            </tbody>
                                            </table>
                                    </c:if>
                                    </td>
                                    </tr>
                   
                   </table>
                <html:hidden property="account" styleId="accountId"/> 
                <html:hidden property="name" styleId="accountNameId"/> 
                <html:hidden property="blueScreenAccount" styleId="blueAccountNameId"/> 
                <html:hidden property="address1" styleId="address1Id"/>
                <html:hidden property="accountType1" styleId="accountType1Id"/> 
                <html:hidden property="accountType3" styleId="accountType3Id"/> 
                <html:hidden property="accountType4" styleId="accountType4Id"/> 
                <html:hidden property="accountType8" styleId="accountType8Id"/> 
                <html:hidden property="accountType9" styleId="accountType9Id"/> 
                <html:hidden property="accountType10" styleId="accountType10Id"/> 
                <html:hidden property="subType" styleId="subTypeId"/>
                <html:hidden property="accountType" styleId="accountTypeId"/>
                <html:hidden property="limit" styleId="limitId"/>
               
            </c:otherwise>
            </c:choose>
            <table>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>
            
            
            

            <html:hidden property="tradingPartnerId"/>
            <html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
            <html:hidden property="accountName" styleId="accountName"/>
            <html:hidden property="accountType" styleId="accountType"/>
            <html:hidden property="subType" styleId="subType"/>
            <html:hidden property="tempaccountType1" styleId="accountType1"/>
            <html:hidden property="tempaccountType2" styleId="accountType2"/>
            <html:hidden property="tempaccountType3" styleId="accountType3"/>
            <html:hidden property="tempaccountType4" styleId="accountType4"/>
            <html:hidden property="tempaccountType5" styleId="accountType5"/>
            <html:hidden property="tempaccountType6" styleId="accountType6"/>
            <html:hidden property="tempaccountType7" styleId="accountType7"/>
            <html:hidden property="tempaccountType8" styleId="accountType8"/>
            <html:hidden property="tempaccountType9" styleId="accountType9"/>
            <html:hidden property="tempaccountType10" styleId="accountType10"/>
            <html:hidden property="tempaccountType11" styleId="accountType11"/>
            <html:hidden property="tempaccountType12" styleId="accountType12"/>
            <html:hidden property="master" styleId="master"/>
            <html:hidden property="frieghtFmc" styleId="frieghtFmc"/>
            <html:hidden property="importQuoteColoadRetail" styleId="importQuoteColoadRetail"/>
            <html:hidden property="unLocCode" styleId="unLocCode"/>
            <html:hidden property="callFrom" styleId="callFrom" value="<%=callFrom%>"/>
            <html:hidden property="field" value="<%=field%>"/>
            <html:hidden property="active" styleId="active"/>
            <html:hidden property="address1" styleId="address1" value="<%=vAddress%>"/>
         <input type="hidden" id="sortByValueForTp" name="sortByValueForTp"/>
        <input type="hidden" id="searchTypeForTp" name="searchTypeForTp" value="${tradingPartnerForm.searchTypeForTp}"/>


        </html:form>
    </body>
    <c:if test="${not empty tradingPartnerForm.account}">
        <script type="text/javascript">checkDisableOrNot('${tradingPartnerForm.account}');</script>
    </c:if>
    <!-- THIS CONDITION IS TO DISABLE THE PAGE BASED ON LOGIN USER ROLE DUTY -->
    <c:if test="${roleDuty.arConfigTabReadOnly eq true}">
        <script type="text/javascript">
            document.getElementById('impCr').disabled='true';
        </script>
    </c:if>
    <script type="text/javascript">
        onLoad('<%=field%>');
        var lclFlags='<%=lclFlag%>';
        if(lclFlags=="lcl"){
            addNew('<%=acctName%>','<%=vAddress%>','<%=vCity%>','<%=vUnlocCode%>');
        }
        function addNewTradingPartner(accountName,accountType,master,subType,active,frieghtFmc,unLocCode,importQuoteColoadRetail){
            document.tradingPartnerForm.accountType.value=accountType;
            document.tradingPartnerForm.master.value=master;
            document.tradingPartnerForm.subType.value=subType;
            document.tradingPartnerForm.unLocCode.value=unLocCode;
            document.tradingPartnerForm.active.value=active;
            document.tradingPartnerForm.frieghtFmc.value=frieghtFmc;
            document.tradingPartnerForm.accountName.value=accountName;
            document.tradingPartnerForm.importQuoteColoadRetail.value = importQuoteColoadRetail;
            document.tradingPartnerForm.callFrom.value=document.getElementById('callFrom').value;
            document.tradingPartnerForm.address1.value=document.getElementById('address1').value;
            document.tradingPartnerForm.buttonValue.value="addNewTradingPartner";
            document.tradingPartnerForm.submit();
        }

        function disableTradingPartner(acctNo, rowIndex) {
            var forwardAcctNo = jQuery.trim(jQuery("#forwardAccount"+rowIndex).val());
            if (forwardAcctNo === "") {
                jQuery.prompt("Please enter forward Account");
            } else {
                jQuery.prompt("Do you want to disable this account and merge it with " + forwardAcctNo + "?", {
                    buttons: {
                        Yes: true,
                        No: false
                    },
                    callback: function (v) {
                        if (v) {
                            jQuery.ajaxx({
                                data: {
                                    className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                                    methodName: "disableTradingPartner",
                                    param1: acctNo,
                                    param2: forwardAcctNo,
                                    param3: "false",
                                    request: true
                                },
                                preloading: true,
                                success: function (data) {
                                    if (jQuery.trim(data).indexOf("Cannot disable the account due to following reasons") >= 0) {
                                        jQuery.prompt(data);
                                    } else if (jQuery.trim(data).indexOf("This account has") >= 0) {
                                        jQuery.prompt(data, {
                                            buttons: {
                                                Yes: true,
                                                No: false
                                            },
                                            callback: function (v) {
                                                if (v) {
                                                    jQuery.ajaxx({
                                                        data: {
                                                            className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                                                            methodName: "disableTradingPartner",
                                                            param1: acctNo,
                                                            param2: forwardAcctNo,
                                                            param3: "true",
                                                            request: true
                                                        },
                                                        preloading: true,
                                                        success: function (data) {
                                                            jQuery("#forwardAccount"+rowIndex).attr('readonly', true);
                                                            jQuery("#forwardAccount"+rowIndex).removeClass("textlabelsBoldForTextBox").addClass("BackgrndColorForTextBox");
                                                            jQuery("#disabledMessage", window.document).html(data);
                                                            jQuery("#DisableImg"+rowIndex).hide();
                                                            jQuery("#EnableImg"+rowIndex).show();
                                                        }
                                                    });
                                                }
                                            }
                                        });
                                    } else {
                                        jQuery("#forwardAccount"+rowIndex).attr('readonly', true);
                                        jQuery("#forwardAccount"+rowIndex).removeClass("textlabelsBoldForTextBox").addClass("BackgrndColorForTextBox");
                                        jQuery("#disabledMessage", window.document).html(data);
                                        jQuery("#DisableImg"+rowIndex).hide();
                                        jQuery("#EnableImg"+rowIndex).show();
                                    }
                                }
                            });
                        }
                    }
                });
            }
        }

        function enableTradingPartner(acctNo,eciAcctNo,eciAcctNo2,eciAcctNo3,sslineNumber, rowIndex,shipperUserName,consigneeUserName,firmsCode) {
            jQuery.prompt("Do you want to enable this account?", {
                buttons: {
                    Yes: true,
                    No: false
                },
                callback: function (v) {
                    if (v) {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.bc.tradingpartner.TradingPartnerBC",
                                methodName: "enableTradingPartner",
                                param1: acctNo,
                                param2: eciAcctNo,
                                param3: eciAcctNo2,
                                param4: eciAcctNo3,
                                param5: sslineNumber,
                                param6: shipperUserName,
                                param7: consigneeUserName,
                                param8: firmsCode,
                                request: true
                            },
                            preloading: true,
                            success: function (data) {
                                if (jQuery.trim(data).indexOf("Account is enabled") < 0) {
                                    jQuery.prompt(data);
                                } else {
                                    jQuery("#forwardAccount"+rowIndex).val("").attr('readonly', false);
                                    jQuery("#forwardAccount"+rowIndex).removeClass("BackgrndColorForTextBox").addClass("textlabelsBoldForTextBox");
                                    jQuery("#disabledMessage", window.document).html(data);
                                    jQuery("#EnableImg"+rowIndex).hide();
                                    jQuery("#DisableImg"+rowIndex).show();
                                }
                            }
                        });
                    }
                }
            });
        }

        function SaveMasterAccoount(indexvalue,AccountNo){
            var  masterAccount = jQuery("#masterAccount"+indexvalue).val();
            var text ="Are you sure you want to select &nbsp <span style=color:red>" +  masterAccount+ " </span> as master account ? ";
            jQuery.prompt(text, {
                buttons: {
                    Yes: 1,
                    No: 2
                },
                submit: function (v) {
                    if (v === 1) {
                        jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
                                methodName: "saveMasterAccountNo",
                                param1 : AccountNo,
                                param2 :masterAccount
                            },
                            success: function (data) {

                            }
                        });
                
                    }else if (v === 2) {
                        jQuery("#masterAccount"+indexvalue).val('');
                        jQuery.prompt.close();

                    }
                }
            });
 
        }

function saveImpCr(index,accNo){
var text ="Are you sure want to change ImportCredit ?";
var importcreditvalue=jQuery("#impCr"+index).is(":checked");

jQuery.prompt(text, {
buttons: {
Yes: 1,
No: 2
},
submit: function (v) {
if (v === 1) {
jQuery.ajaxx({
    data: {
        className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
        methodName: "saveImportCredit",
        param1: accNo,
        param2: importcreditvalue,
        request: true
       
    },
    success: function (data) {

    }
});

}else if (v === 2) {

jQuery.prompt.close();

jQuery.ajaxx({
    data: {
        className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
        methodName: "getImportcreditValue",
        param1: '${tradingPartnerForm.account}'
    },
    success: function (data) {

        if(data == 'Y'){
           
            document.getElementById('impCr'+index).checked = true;
        }else if(data == 'N'){
            document.getElementById("impCr").checked = false;
        }
    }
});

}
}
});
}
function saveEciConsigneeAccounting(index,acctNo){

var valueForeciacct=jQuery("#eciAccountNo2"+index).val();

jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
                                methodName: "saveEciConsigneeAccounting",
                                param1 : valueForeciacct,
                                param2 : acctNo,
                                request: true
                            },
                            success: function (data) {

                            }
                        });
                        }


function saveEciShipperAccounting(index,acctNo){
    
var valueForeciacct = jQuery("#eciAccountNo"+index).val();

jQuery.ajaxx({
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
                                methodName: "saveEciShipperAccounting",
                                param1 : valueForeciacct,
                                param2 : acctNo,
                                request: true
                            },
                            success: function (data) {

                            }
                        });
}
function saveEciVendorAccounting(index,acctNo){

var valueForeciacct=jQuery("#eciAccountNo3"+index).val();

jQuery.ajaxx({
       
                            data: {
                                className: "com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO",
                                methodName: "saveEciVendorAccounting",
                                param1 : valueForeciacct,
                                param2 :acctNo,
                                request: true
                            },
                            success: function (data) {

                            }
                        });
}
 function doSort(val){

    var searchType = jQuery("#searchTypeForTp").val();
            var toggleValue = searchType === "up" ? "down" : "up";
            jQuery("#" + val).removeClass(searchTypeForTp).addClass(toggleValue);
            jQuery("#sortByValueForTp").val(val);
            jQuery("#searchTypeForTp").val(toggleValue);
            document.tradingPartnerForm.buttonValue.value = "SearchMachList";
            document.tradingPartnerForm.submit();
    }

    </script>
    <script type="text/javascript" src="<%=path%>/js/fcl/clientSearch.js"></script>
</html>
