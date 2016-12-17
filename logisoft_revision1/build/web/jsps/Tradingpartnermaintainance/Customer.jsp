<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://displaytag.sf.net" prefix="display"%>
<%@taglib uri="/WEB-INF/tlds/string.tld" prefix="string"%>
<%@taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<%@include file="../includes/jspVariables.jsp"%>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@include file="../includes/resources.jsp"%>
<%@include file="../includes/baseResources.jsp"%>
<%@include file="../fragment/formSerialize.jspf" %>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}">
        <title>Customer Address</title>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/isValidEmail.js"></script>
        <style type="text/css">
            div.autocomplete ul{
                width: 400px;
                height: 200px;
            }
        </style>
    </head>
    <body class="whitebackgrnd" onload="getNotifyParty('${CUST_ADDRESS.notifyParty}',parent.document.getElementById('accountType').value.indexOf('C')>=0)">
        <un:useConstants className="com.gp.cong.common.CommonConstants" var="commonConstants"/>
        <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <html:form action="/tradingPartner" styleId="customer" scope="request">
            <html:hidden property="index" value="${CUST_ADDRESS.id}"/>
            <html:hidden property="accountName" value="<%=accountName%>"/>
            <table width="100%" border="0" cellpadding="0" cellspacing="5">
                <c:if test="${not empty ADD_ADDRESS}">
                    <tr>
                        <td>
                            <table width="100%" border="0" cellpadding="1" cellspacing="0" class="tableBorderNew">
                                <tr class="tableHeadingNew"><td colspan="3">Add Customer Address</td></tr>
                                <tr>
                                    <td>
                                        <table width="100%" border="0" cellpadding="1" cellspacing="0">
                                            <tr class="textlabelsBold">
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty CUST_ADDRESS.primary && fn:toUpperCase(CUST_ADDRESS.primary)==fn:toUpperCase(commonConstants.ON)}">
                                                            <html:checkbox property="primary" styleId="primary" name="CUST_ADDRESS"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="checkbox" id="primary" name="primary">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    Primary
                                                </td>
                                                <td id="nParty">
                                                    <c:choose>
                                                        <c:when test="${not empty CUST_ADDRESS.notifyParty && fn:toUpperCase(CUST_ADDRESS.notifyParty)==fn:toUpperCase(commonConstants.ON)}">
                                                            <html:checkbox property="notifyParty" styleId="notifyParty" name="CUST_ADDRESS"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <html:checkbox property="notifyParty" styleId="notifyParty"/>
                                                        </c:otherwise>
                                                    </c:choose>
                                                    Notify Party
                                                </td>
                                                <td>
                                                    <c:choose>
                                                        <c:when test="${not empty CUST_ADDRESS.checkAddress}">
                                                            <html:checkbox property="checkAddress" styleId="checkAddress" name="CUST_ADDRESS"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            <input type="checkbox" id="checkAddress" name="checkAddress">
                                                        </c:otherwise>
                                                    </c:choose>
                                                    Check Delivery
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>C/O Name</td>
                                                <td>
                                                    <html:text property="coName" value="${CUST_ADDRESS.coName}" styleId="coName" onchange="onchangePrimaryAddress()"
                                                               styleClass="textlabelsBoldforTextBox" maxlength="40" size="44" style="text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Contact Name</td>
                                                <td>
                                                    <html:text property="contactName" value="${CUST_ADDRESS.contactName}" styleClass="textlabelsBoldforTextBox" maxlength="18" style="width:122px;text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Address </td>
                                                <td>
                                                    <html:textarea property="address1" value="${CUST_ADDRESS.address1}" styleClass="textlabelsBoldforTextBox" cols="48" rows="2" onchange="onchangePrimaryAddress()"
                                                                   styleId="address1" style="text-transform: uppercase;overflow:hidden" onkeypress="return checkTextAreaLimit(this, 100)"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table width="100%" border="0" cellpadding="1" cellspacing="0">
                                            <tr class="textlabelsBold">
                                                <td>City</td>
                                                <td>
                                                    <html:text property="city" styleId="city" value="${CUST_ADDRESS.city2}" onchange="onchangePrimaryAddress()"
                                                               styleClass="textlabelsBoldforTextBox" style="width:122px;text-transform: uppercase;" size="16"/>
                                                    <input type="hidden"name="cityCheck" id="cityCheck" value="${CUST_ADDRESS.city2}">
                                                    <div id="cityChoices" class="autocomplete"></div>
                                                    UNLOC
                                                    <html:text property="unLocCode" styleId="unLocCode" value="${CUST_ADDRESS.unLocCode}" size="5" styleClass="textlabelsBoldforTextBox" style="text-transform: uppercase;"/>
                                                    <input type="hidden"name="unLocCodeCheck" id="unLocCodeCheck" value="${CUST_ADDRESS.unLocCode}">
                                                    <div id="unLocCode_choices" class="newAutocomplete"></div>
                                                    <script type="text/javascript">
                                                        initAutocompleteWithFormClear("unLocCode","unLocCode_choices","","unLocCodeCheck",
                                                        "${path}/actions/getUnlocationCode.jsp?tabName=CUSTOMER_ADDRESS&isDojo=false","","");
                                                    </script>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Zip</td>
                                                <td>
                                                    <html:text property="zip" styleId="zip" value="${CUST_ADDRESS.zip}" onkeyup="zipcode(this);" maxlength="10"
                                                               onchange="onchangePrimaryAddress()" styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Phone </td>
                                                <td>
                                                    <html:text property="phone" styleId="phone" value="${CUST_ADDRESS.phone}" maxlength="13" styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;"/>
                                                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Ext
                                                    <html:text property="extension" styleId="extension" value="${CUST_ADDRESS.extension}" maxlength="4" styleClass="textlabelsBoldForTextBox" size="2" style="text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Email1</td>
                                                <td>
                                                    <html:text property="email1" styleId="email1" value="${CUST_ADDRESS.email1}" maxlength="40" styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                    <td>
                                        <table width="100%" border="0" cellpadding="1" cellspacing="0">
                                            <tr class="textlabelsBold">
                                                <td>State</td>
                                                <td>
                                                    <html:text property="state" styleId="state" value="${CUST_ADDRESS.state}" onchange="onchangePrimaryAddress()"
                                                               styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;" maxlength="2"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Country</td>
                                                <td>
                                                    <html:text property="country" styleId="country" value="${CUST_ADDRESS.cuntry.codedesc}" styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;" />
                                                    <html:hidden property="countryCode" styleId="countryCode" value="${CUST_ADDRESS.cuntry.id}"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Fax</td>
                                                <td>
                                                    <html:text property="fax" styleId="fax" value="${CUST_ADDRESS.fax}" styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                            <tr class="textlabelsBold">
                                                <td>Email2</td>
                                                <td>
                                                    <html:text property="email2" styleId="email2" value="${CUST_ADDRESS.email2}" styleClass="textlabelsBoldForTextBox" style="width:122px;text-transform: uppercase;"/>
                                                </td>
                                            </tr>
                                        </table>
                                    </td>
                                </tr>
                                <tr>
                                    <td align="center" colspan="3">
                                        <input type="button" class="buttonStyleNew" id="saveBtn" value="Save" onclick="save(${CUST_ADDRESS.id})"/>
                                        <input type="button" class="buttonStyleNew" value="Cancel" onclick="reloadSearchPage()"/>
                                    </td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </c:if>
                <tr>
                    <td>
                        <table width="100%" cellpadding="0" cellspacing="0" border="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">
                                <td>List of Customer Addressess</td>
                                <td align="right">
                                    <c:if test="${empty ADD_ADDRESS && view!='3'}">
                                        <input type="button" class="buttonStyleNew" value="Add" onclick="addNew()" size="16"/>
                                    </c:if>
                                </td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <div class="scrolldisplaytable">
                                        <c:choose>
                                            <c:when test="${not empty TRADINGPARTNER.customerAddressSet}">
                                                <display:table name="${TRADINGPARTNER.customerAddressSet}" pagesize="<%=pageSize%>" class="displaytagstyleNew" id="customerAddress" sort="list" style="width:100%">
                                                    <display:setProperty name="paging.banner.some_items_found">
                                                        <span class="pagebanner"> <font color="blue">{0}</font>
                                                            Customer Addresses displayed,For more Customers click on page numbers.
                                                        </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.one_item_found">
                                                        <span class="pagebanner"> One {0} displayed. Page Number </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.all_items_found">
                                                        <span class="pagebanner"> {0} {1} displayed, Page Number </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="basic.msg.empty_list">
                                                        <span class="pagebanner"> No Customer Address Found. </span>
                                                    </display:setProperty>
                                                    <display:setProperty name="paging.banner.placement" value="bottom"/>
                                                    <display:setProperty name="paging.banner.item_name" value="Customer Address"/>
                                                    <display:setProperty name="paging.banner.items_name" value="Customer Addresses"/>
                                                    <display:column title="Primary" style="color:red;font-size:25px;text-align:center;">
                                                        <c:if test="${not empty customerAddress.primary && fn:toUpperCase(customerAddress.primary)==fn:toUpperCase(commonConstants.ON)}">
                                                            <c:out value="*"/>
                                                        </c:if>
                                                    </display:column>
                                                    <display:column title="C/O Name" property="coName"/>
                                                    <display:column title="Address">
                                                        ${string:splitter(customerAddress.address1,100,'<br>')}
                                                    </display:column>
                                                    <display:column title="City" property="city2"/>
                                                    <display:column title="State" property="state"/>
                                                    <display:column title="Country" property="cuntry.codedesc"/>
                                                    <display:column title="Action">
                                                        <c:if test="${view!='3'}">
                                                            <img alt="" src="${path}/img/icons/edit.gif" onclick="editCustomer('${customerAddress.id}')" onmouseover="tooltip.show('<strong>Edit</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                                            <img alt="" src="${path}/img/icons/delete.gif" onclick="deleteCustomer('${customerAddress.id}')" onmouseover="tooltip.show('<strong>Delete</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                                            <img alt="" src="${path}/img/icons/e_contents_view.gif" onclick="showNotes('${customerAddress.id}')" onmouseover="tooltip.show('<strong>Notes</strong>',null,event);" onmouseout="tooltip.hide();"/>
                                                        </c:if>
                                                    </display:column>
                                                </display:table>
                                            </c:when>
                                            <c:otherwise>
                                                <div class="pagebanner">No Customer Address Found.</div>
                                            </c:otherwise>
                                        </c:choose>

                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>

            <html:hidden property="tradingPartnerId" value="<%=accountNo%>"/>
            <html:hidden property="accountNo" styleId="accountNo" value="${tradingPartnerForm.accountNo}"/>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
    <script type="text/javascript">
        function getNotifyParty(val,accountType4Checked){
            if(undefined != document.getElementById('nParty')){
                if(accountType4Checked){
                    document.getElementById('nParty').style.display='block';
                    if(val.toLowerCase == 'on' && document.tradingPartnerForm.notifyParty){
                        document.tradingPartnerForm.notifyParty.checked=true;
                    }
                }else{
                    document.getElementById('nParty').style.display='none';
                }
            }
        }
        function save(updateValue){
            if(document.tradingPartnerForm.address1.value==""){
                alert("please enter the address");
                document.tradingPartnerForm.address1.value="";
                document.tradingPartnerForm.address1.focus();
                return;
            }
            if(document.tradingPartnerForm.city.value==""){
                alert("please enter the city");
                document.tradingPartnerForm.city.value="";
                document.tradingPartnerForm.city.focus();
                return;
            }
            var value=document.tradingPartnerForm.zip.value;
            for(var i=0;i< value.length;i++){
                if(value.indexOf(" ") == 0){
                    alert("Please dont start with white space");
                    return;
                }
            }
            if(document.tradingPartnerForm.zip.value!="" && document.tradingPartnerForm.zip.value.length<5){
                alert("Zipcode should be 5 digits.");
                document.tradingPartnerForm.zip.value="";
                document.tradingPartnerForm.zip.focus();
                return;
            }
            var value=document.tradingPartnerForm.phone.value;
            for(var i=0;i< value.length;i++){
                if(value.indexOf(" ") == 0){
                    alert("Please dont start with white space");
                    return;
                }
            }
            var value=document.tradingPartnerForm.fax.value;
            for(var i=0;i< value.length;i++){
                if(value.indexOf(" ") == 0){
                    alert("Please dont start with white space");
                    return;
                }
            }
            if(updateValue){
                document.tradingPartnerForm.buttonValue.value="updateCustomerAddress";
            }else{
                document.tradingPartnerForm.buttonValue.value="addCustAdrress";
            }
            document.getElementById("saveBtn").disabled = true;
            document.tradingPartnerForm.submit();
        }
        function phonevalid(obj){
            getIt(obj);
        }
        function faxvalid(obj){
            getIt(obj);
        }
        function zipcode(obj) {
            if (document.tradingPartnerForm.zip.value == "" && document.tradingPartnerForm.country.value != "UNITED STATES") {
                if ((document.tradingPartnerForm.zip.value.length > 5) || IsNumeric(document.tradingPartnerForm.zip.value.replace(/ /g, "")) == false) {
                    alert("please enter the only 5 digits and numerics only");
                    document.tradingPartnerForm.zip.value = "";
                    document.tradingPartnerForm.zip.focus();
                }
            } else {
                getzip(obj);
            }
        }
        function deleteCustomer(index){
            document.tradingPartnerForm.buttonValue.value="deleteCustomerAddress";
            document.tradingPartnerForm.index.value=index;
            if(confirm("Are you sure you want to delete this Address")){
                document.tradingPartnerForm.submit();
            }
        }
        function editCustomer(index){
            document.tradingPartnerForm.buttonValue.value="editCustomerAddress";
            document.tradingPartnerForm.index.value=index;
            document.tradingPartnerForm.submit();
        }
        function addNew(){
            document.tradingPartnerForm.buttonValue.value="addNewCustomerAddress";
            document.tradingPartnerForm.submit();
        }
        function reloadSearchPage(){
            document.tradingPartnerForm.buttonValue.value="cancelCustomerAddress";
            document.tradingPartnerForm.submit();
        }
        
        function checkForAddressDetails(){
            if(tableLength.length==0){
                alert("Please enter the Address Information in Address Tab");
                return false;
            }else{
                return true;
            }
        }

        function showNotes(id){
            GB_show('Notes', rootPath+'/notes.do?moduleId=${notesConstants.CUSTOMERADDRESS}&moduleRefId='+id,375,700);
        }
        if(document.getElementById("city")){
            new Ajax.ScrollAutocompleter("city", "cityChoices", rootPath+"/servlet/AutoCompleterServlet?action=City&textFieldId=city", {
                paramName: "city",
                tokens: "/",
                afterUpdateElement : function (text, li) {
                    var list = li.id.split("@");
                    $("city").value = list[0].replace("&#39;", "'");
                    $("cityCheck").value = list[0];
                    $("unLocCode").value = list[1];
                    $("unLocCodeCheck").value = list[1];
                    $("state").value = list[2];
                    $("country").value = list[3];
                    $("city").blur();
                }
            });

            Event.observe("city", "blur", function (event){
                var element = Event.element(event);
                if(element.value!==$("cityCheck").value){
                    $("city").value = "";
                    $("cityCheck").value = "";
                    $("unLocCode").value = "";
                    $("unLocCodeCheck").value = "";
                    $("state").value = "";
                    $("country").value = "";
                }
            });
        }
        function onchangePrimaryAddress() {
            var coName = document.getElementById('coName').value.toUpperCase();
            var address1 = document.getElementById('address1').value.toUpperCase();
            var city = document.getElementById('city').value.toUpperCase();
            var zip = document.getElementById('zip').value.toUpperCase();
            var state = document.getElementById('state').value.toUpperCase();
            parent.document.aciframe.document.getElementById('coName').value = coName;
            parent.document.aciframe.document.getElementById('address1').value = address1;
            parent.document.aciframe.document.getElementById('city').value = city;
            parent.document.aciframe.document.getElementById('zip').value = zip;
            parent.document.aciframe.document.getElementById('state').value = state;
}
    </script>
    <c:if test="${view=='3'}">
        <script type="text/javascript">parent.parent.parent.parent.makeFormBorderless(document.getElementById("customer"))</script>
    </c:if>
    <script type="text/javascript">serializeForm();</script>
</html>
