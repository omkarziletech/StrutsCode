<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.domain.TradingPartner,com.gp.cong.logisoft.bc.tradingpartner.*,com.gp.cong.logisoft.domain.Customer,com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerTemp,java.util.*,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.beans.customerBean"%>
<jsp:directive.page import="org.apache.commons.lang3.StringUtils"/>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String buttonValue = "";
            String amsg = "";
            String account1 = "off";
            String acctName = "";
            String acctNo = "";
            int i = 0;
            String fromMasterValue = "YES";
            if (session.getAttribute("trade") != null) {
                session.removeAttribute("trade");
            }

            if (session.getAttribute("amsg") != null) //if starts here 4 checkin amsg,if amsg contains data its displayed
            {
                amsg = (String) session.getAttribute("amsg");
                if (amsg != "") {
%>
<script type="text/javascript">
    alert(<%=amsg%>);// used to display the supplied msg of amsg
</script>
<%}
            }//if ends here

            if (request.getAttribute("buttonValue") != null) {
                buttonValue = (String) request.getAttribute("buttonValue");
            }

            String message = "";
            List customerList = null;

            String modify = "";

            customerBean customerbean = new customerBean(); //creating object for customer bean

            if (session.getAttribute("customerbean") != null) //if starts here
            {
                customerbean = (customerBean) session.getAttribute("customerbean");
                if (customerbean.getName() != null) {
                    acctName = customerbean.getName();
                }
                if (customerbean.getAccountNo() != null) {
                    acctNo = customerbean.getAccountNo();
                }
            }// if ends here
            request.setAttribute("customerbean", customerbean);
            request.setAttribute("customer", customer);

            if (session.getAttribute("message") != null) //if starts 4 checking nullness for message
            {
                message = (String) session.getAttribute("message");
            } //if ends

            if (session.getAttribute("customerList") != null)//if starts 4 checking nullness for customerList
            {
                customerList = (List) session.getAttribute("customerList");
            }  //if ends

            if (request.getParameter("modify") != null)//
            {
                modify = (String) request.getParameter("modify");
                session.setAttribute("modifyforcustomer", modify);
            } else {
                modify = (String) session.getAttribute("modifyforcustomer");
            }//

            if (request.getParameter("programid") != null) {
                String programId = request.getParameter("programid");
                session.setAttribute("processinfoforcustomer", programId);
            }
            String type1 = "mb";

            if (session.getAttribute("tradingPartner") != null)//to retrive & display the record been added
            {
                TradingPartner tradingpartner = (TradingPartner) session.getAttribute("tradingPartner");
                tradingpartner.getAccountno();

            }
            if (session.getAttribute("tradingPartner") != null) {
                session.removeAttribute("tradingPartner");
            }
            session.setAttribute("customerList", customerList);
            String editPath = path + "/searchCustomer.do";
            request.setAttribute("sortByList", dbUtil.getMasterSortByList());
            request.setAttribute("limitList", dbUtil.limitList());
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>JSP for CustomerForm form</title>

        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <script type="text/javascript">
            function showToolTip(enabled){
                if(enabled){
                    tooltip.show("Checked = AutoComplete Enabled",null,event);
                }else{
                    tooltip.show("UnChecked = AutoComplete Disabled",null,event);
                }
            }
            function searchform(){
                if(document.tradingPartnerForm.name.value==''){
                    document.tradingPartnerForm.name.value = document.tradingPartnerForm.ord_name.value;
                }
                if(document.tradingPartnerForm.name.value=="" && document.tradingPartnerForm.account.value==""
                    && document.tradingPartnerForm.searchAddress.value=="" && !(document.tradingPartnerForm.accountType1.checked)
                    &&!(document.getElementById('accountType1').checked) && !(document.getElementById('accountType3').checked)
                    && !(document.getElementById('accountType4').checked) && !(document.getElementById('accountType8').checked) && !(document.getElementById('accountType9').checked)
                    && !(document.getElementById('accountType10').checked) && !(document.getElementById('accountType11').checked)){
                    alert("Enter Search Criteria");
                    document.tradingPartnerForm.name.value=="";
                    document.tradingPartnerForm.name.focus();
                    return;
                }
                document.tradingPartnerForm.buttonValue.value="searchMasterCustomers";
                document.tradingPartnerForm.submit();
            }
            function saveNewCustomer(){
                if(document.tradingPartnerForm.accountName.value==""){
                    alert("Please enter the Account Name");
                    return;
                }
                document.tradingPartnerForm.buttonValue.value="saveMasterCustomer";
                document.tradingPartnerForm.submit();
            }//
            function viewCustomer(val1){
                document.tradingPartnerForm.tradingPartnerId.value=val1;
                document.tradingPartnerForm.buttonValue.value="editMasterCustomer";
                document.tradingPartnerForm.submit();
            }
            function moreInfoCustomer(val1){
                document.tradingPartnerForm.tradingPartnerId.value=val1;
                document.tradingPartnerForm.buttonValue.value="moreInfoCustomer";
                document.tradingPartnerForm.submit();
            }
            function setAutoComplete(enabled){
                if(enabled){
                    document.getElementById("autoCompleteDiv").style.display="block";
                    document.getElementById("nameTextBox").style.display="none";
                }else{
                    document.getElementById("autoCompleteDiv").style.display="none";
                    document.getElementById("nameTextBox").style.display="block";
                }
            }
            function reloadPage() {
                if(document.getElementById('pageReloadFlag').value != "false") {
                    window.location.reload();
                }
                window.detachEvent("onload");
                document.getElementById('pageReloadFlag').value = "false"
            }
            function addNew() {
                GB_show('Add Customer', '<%=path%>/jsps/Tradingpartnermaintainance/AddTradingPartner.jsp?master=yes',350,850);
            }
            function search(){
                document.tradingPartnerForm.buttonValue.value="removesessionmaster";
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
            function getUppercaseAcctNo(val){
                val=val.toUpperCase();
                document.tradingPartnerForm.account.value=val;
            }
            function getUppercaseName(val){
                val=val.toUpperCase();
                document.tradingPartnerForm.ord_name.value=val;
            }
            function searchform1(){
                if(document.tradingPartnerForm.accountName.value==""){
                    alert("Please enter the Account Name");
                    return;
                }

                document.tradingPartnerForm.buttonValue.value="saveMasterCustomer";
                document.tradingPartnerForm.submit();
            }
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/tradingPartner"  name="tradingPartnerForm" type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
            <font color="red" size="4" id="allReadyExist"></font>
            <font color="blue" size="4"><%=message%></font>
            <c:choose>
                <c:when test="${empty sessionScope.tradingPartnerMasterSearchList}">
                    <table width=100% border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                        <tr class="tableHeadingNew" colspan="2"><td>Search Customer</td>
                            <td align="right">
                                <input type="button" class="buttonStyleNew" value="Add New" id="addNewCustomerToggle" onclick="addNew()"/>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <div id="search_customer_vertical_slide" >
                                    <table width="100%" border="0" cellpadding="0" cellspacing="10">
                                        <tr class="textlabelsBold">
                                            <td>Name</td>
                                            <td>
                                                <div id="autoCompleteDiv">
                                                    <input name="name" class="textlabelsBoldForTextBox"  id="name" value="<%=acctName%>" size="45" onkeydown="getUppercase(this.value)" autocomplete="false"/>
                                                    <input name="name_check" id="name_check" type="hidden" value="<%=acctName%>" />
                                                    <div id="name_choices" style="display: none" class="autocomplete"></div>
                                                </div>
                                                <div id="nameTextBox" >
                                                    <input name="ord_name" class="textlabelsBoldForTextBox" id="ord_name" value="<%=acctName%>" onkeydown ="getUppercaseName(this.value)" size="45"  />
                                                </div>
                                            </td>
                                            <td width="7%" align="right">Account # </td>
                                            <td align="left" valign="top">
                                                <input name="account" class="textlabelsBoldForTextBox" id="account" value="<%=acctNo%>" size="45" onkeydown ="getUppercaseAcctNo(this.value)"/>
                                                <input name="account_check" id="account_check" type="hidden" value="<%=acctNo%>" />
                                                <div id="account_choices" style="display: none" class="autocomplete"></div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td colspan="2" class="textlabelsBold"><input type=checkbox  onclick="setAutoComplete(this.checked);" > Enable AutoComplete Feature</td>
                                            <td class="textlabelsBold" align="right">Address
                                            </td>
                                            <td>
                                                <html:text property="searchAddress" styleClass="textlabelsBoldForTextBox" size="45" onkeydown ="getUppercaseAddress(this.value)"></html:text>
                                            </td>
                                        </tr>
                                    </table>
                                    <table width="65%" border="0">
                                        <tr class="textlabelsBold">
                                            <td >Account Type </td>
                                            <td><span onmouseover="tooltip.show('Shipper',null,event)" onmouseout="tooltip.hide();">S</span></td>
                                            <td width="1%" align="left"><html:checkbox property="accountType1" styleId="accountType1" name="customerbean" ></html:checkbox></td>
                                            <%--<td><span onmouseover="tooltip.show('Forwarder')" onmouseout="tooltip.hide();">F</span></td>
                                            <td><html:checkbox property="accountType2" styleId="accountType2" name="customerbean"  /></td>--%>
                                            <td><span onmouseover="tooltip.show('NOVCC',null,event)" onmouseout="tooltip.hide();">N</span></td>
                                            <td><html:checkbox property="accountType3" styleId="accountType3"  name="customerbean"/></td>
                                            <td><span onmouseover="tooltip.show('Consignee',null,event)" onmouseout="tooltip.hide();">C</span></td>
                                            <td><html:checkbox property="accountType4" styleId="accountType4" name="customerbean"/></td>
                                            <td><span onmouseover="tooltip.show('Import Agent',null,event)" onmouseout="tooltip.hide();">AI</span></td>
                                            <td><html:checkbox property="accountType8" styleId="accountType8" name="customerbean"/></td>
                                            <td><span onmouseover="tooltip.show('Export Agent',null,event)" onmouseout="tooltip.hide();">AE</span></td>
                                            <td><html:checkbox property="accountType9" styleId="accountType9" name="customerbean"/></td>
                                            <td><span onmouseover="tooltip.show('Vendor',null,event)" onmouseout="tooltip.hide();">V</span></td>
                                            <td><html:checkbox property="accountType10" styleId="accountType10" name="customerbean"/></td>
                                            <td><span onmouseover="tooltip.show('Others',null,event)" onmouseout="tooltip.hide();">O</span></td>
                                            <td><html:checkbox property="accountType11" styleId="accountType11" /></td>
                                            <td>Sort By</td>
                                            <td><html:select property="sortBy" value="${tradingPartnerForm.sortBy}" styleClass="dropdown_accounting">
                                                    <html:optionsCollection name="sortByList"/>
                                                </html:select></td>
                                            <td>Limit</td>
                                            <td><html:select property="limit" value="${tradingPartnerForm.limit}" styleClass="dropdown_accounting">
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
                                <input type="button" class="buttonStyleNew" value="Search" 	id="addNewCustomerToggle" onclick="search()"/>
                            </td>
                        </tr>
                        <tr>
                            <td><table width="100%">
                                    <tr class="textlabelsBold"><td>Name</td>
                                        <td>Account No</td>
                                        <td>Account Type</td>
                                        <td>Sort By</td>
                                    </tr>
                                    <tr class="textlabelsBold">
                                        <td><c:out value="${tradingPartnerForm.name}"/></td>
                                        <td><c:out value="${tradingPartnerForm.account}"></c:out></td>
                                        <td><c:if test="${tradingPartnerForm.accountType1=='on'}"><c:out value="S"/></c:if>
                                            <%--<c:if test="${tradingPartnerForm.accountType2=='on'}"><c:out value="F"/></c:if>--%>
                                            <c:if test="${tradingPartnerForm.accountType3=='on'}"><c:out value="N"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType4=='on'}"><c:out value="C"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType5=='on'}"><c:out value="SS"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType6=='on'}"><c:out value="T"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType7=='on'}"><c:out value="A"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType8=='on'}"><c:out value="I"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType9=='on'}"><c:out value="E"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType10=='on'}"><c:out value="V"/></c:if>
                                            <c:if test="${tradingPartnerForm.accountType11=='on'}"><c:out value="O"/></c:if></td>
                                        <td><c:if test="${tradingPartnerForm.sortBy=='1'}"><c:out value="Account Name"/></c:if>
                                            <c:if test="${tradingPartnerForm.sortBy=='2'}"><c:out value="Account No"/></c:if>
                                            <c:if test="${tradingPartnerForm.sortBy=='5'}"><c:out value="Address"/></c:if></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </c:otherwise>
            </c:choose>
            <table>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <table border="0" width="100%" class="tableBorderNew">
                <tr class="tableHeadingNew" colspan="2">Search Results</tr>
                <tr>
                    <td>
                        <%
                                    List list = new ArrayList();
                                    String customerName = "";
                                    String custName = "";
                                    String address = "";
                                    String addr = "";

                                    if (session.getAttribute("tradingPartnerMasterSearchList") != null) {
                                        list = (List) session.getAttribute("tradingPartnerMasterSearchList");
                                    }


                        %>
                        <div id="divtablesty1" class="scrolldisplaytable">
                            <table  border="0" cellpadding="0" cellspacing="0">

                                <display:table name="<%=list%>" pagesize="<%=pageSize%>" class="displaytagstyleNew" id="customertable" sort="list" defaultorder="ascending" defaultsort="${tradingPartnerForm.sortBy}">

                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Customer details displayed,For more Customers click on page numbers.
                                            <br>
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
                                    <display:setProperty name="paging.banner.item_name" value="Customer"/>
                                    <display:setProperty name="paging.banner.items_name" value="Customers"/>
                                    <%

                                                if (list != null && list.size() > 0) {

                                                    TradingPartnerInfo tradingPartnerInfo = (TradingPartnerInfo) list.get(i);
                                                    request.setAttribute("tradingPartnerInfo", tradingPartnerInfo);
                                                    if (tradingPartnerInfo.getAccountName() != null && !tradingPartnerInfo.getAccountName().equals("null")) {
                                                        customerName = tradingPartnerInfo.getAccountName();
                                                        custName = StringUtils.abbreviate(customerName, 15);
                                                    }
                                                    if (tradingPartnerInfo.getAddress() != null && !tradingPartnerInfo.getAddress().equals("")) {
                                                        address = tradingPartnerInfo.getAddress();
                                                        addr = StringUtils.abbreviate(address, 15);
                                                    }
                                                }
                                    %>

                                    <display:column title="Customer Name" sortable="true" >
                                        <span class="hotspot" onmouseover="tooltip.show('<strong><%=customerName%></strong>',null,event);" onmouseout="tooltip.hide();">
                                            <a href="#" onclick="moreInfoCustomer('${customertable.accountNumber}')"><%=custName%></a></span>
                                        </display:column>
                                        <display:column property="accountNumber" title="Acct#" sortable="true"/>
                                        <display:column title="Type" sortable="true" property="accountType"></display:column>
                                        <display:column title="C/O Name" sortable="true" property="coName"></display:column>
                                        <display:column title="ECI Shpr/FF#" sortable="true" property="eciAcctNo"></display:column>
                                        <display:column title="ECI Consignee" sortable="true" property="eciAcctNo2"></display:column>
                                        <display:column title="ECI Vendor" sortable="true" property="eciAcctNo3"></display:column>
                                        <display:column title="Addr" sortable="true" >
                                        <span class="hotspot" onmouseover="tooltip.show('<strong><%=address%></strong>',null,event);" onmouseout="tooltip.hide();"><%=addr%></span>
                                    </display:column>
                                    <display:column title="City" sortable="true" property="city"></display:column>
                                    <display:column title="ST" sortable="true" property="state"></display:column>
                                    <display:column title="CR">
                                        <html:checkbox property="credit" onclick="return false;" name="tradingPartnerInfo" ></html:checkbox>
                                    </display:column>
                                    <display:column title="HD">
                                        <html:checkbox property="hold" onclick="return false;" name="tradingPartnerInfo" ></html:checkbox>
                                    </display:column>
                                    <display:column title="IN" >
                                        <html:checkbox property="insurance" onclick="return false;" name="tradingPartnerInfo" ></html:checkbox>
                                    </display:column>
                                    <display:column title="Actions">
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>edit</strong>',null,event);"
                                              onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/edit.gif" border="0"
                                                                          onclick="viewCustomer('${customertable.accountNumber}')" /></span>
                                        <span class="hotspot" onmouseover="tooltip.show('<strong>Subsidiaries</strong>',null,event);"
                                              onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/contextgroup.gif" border="0"
                                                                          onClick="return GB_show('Subsidiaries', '<%=path%>/tradingPartner.do?buttonValue=showSubsidiaries&master=${customertable.accountNumber}',300,700);" /></span>
                                            <%--<span class="hotspot" onmouseover="tooltip.show('<strong>MOre Info</strong>');"
				onmouseout="tooltip.hide();"><img src="<%=path%>/img/icons/pubserv.gif" border="0"
				onclick="moreInfoCustomer('${customertable.accountNumber}')" /></span>--%>
                                        </display:column>
                                        <%i++;%>
                                    </display:table>
                            </table></div>
                    </td>
                </tr>
            </table>
            <html:hidden property="tradingPartnerId"/>
            <html:hidden property="buttonValue" styleId="buttonValue" value="<%=buttonValue%>"/>
            <html:hidden property="accountType"/>
            <html:hidden property="fromMaster" value="<%=fromMasterValue%>"/>
            <html:hidden property="accountName" styleId="accountName"/>
            <html:hidden property="subType" styleId="subType"/>
            <html:hidden property="master" styleId="master"/>
            <html:hidden property="frieghtFmc" styleId="frieghtFmc"/>
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
            <input type="hidden" name="pageReloadFlag" id="pageReloadFlag"/>
            <html:hidden property="active"/>
        </html:form>
        <script type="text/javascript">document.getElementById("autoCompleteDiv").style.display="none";</script>
    </body>
    <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
    <script type="text/javascript" src="<%=path%>/js/common.js"></script>
    <script type="text/javascript" src="<%=path%>/js/script.aculo.us/autocomplete.js"></script>
    <script type="text/javascript">
        function addNewTradingPartner(accountName,accountType,master,subType,active,frieghtFmc){
            document.tradingPartnerForm.accountName.value=accountName;
            document.tradingPartnerForm.accountType.value=accountType;
            document.tradingPartnerForm.master.value=master;
            document.tradingPartnerForm.subType.value=subType;
            document.tradingPartnerForm.active.value=active;
            document.tradingPartnerForm.frieghtFmc.value=frieghtFmc;
            document.tradingPartnerForm.buttonValue.value="addNewTradingPartner";
            document.tradingPartnerForm.submit();
        }
        AjaxAutocompleter("name", "name_choices", "", "name_check",
        "<%=path%>/actions/getTradingPartnerAccountName.jsp?tabName=MASTER_SEARCH_CUSTOMER&from=1&isDojo=false", "");
        AjaxAutocompleter("account", "account_choices", "", "account_check",
        "<%=path%>/actions/getAccountNo.jsp?tabName=MASTER_SEARCH_CUSTOMER&from=1&isDojo=false", "");
    </script>
</html>



