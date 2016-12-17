<%@ page language="java"  import="com.gp.cvst.logisoft.util.DBUtil"%>
<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.*,java.util.*,com.gp.cong.logisoft.domain.*,com.gp.cong.logisoft.beans.customerBean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html:html>
    <head>
        <title> City Search </title>
        <%
                    List searchList = null;
                    String path = request.getContextPath();
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                    if (path == null) {
                        path = "../..";
                    }
                    customerBean custBean = new customerBean();
                    if (session.getAttribute("SearchCustList") != null) {
                        searchList = (List) session.getAttribute("SearchCustList");
                        session.removeAttribute("SearchCustList");
                    }
                    com.gp.cvst.logisoft.util.DBUtil dbutil = new com.gp.cvst.logisoft.util.DBUtil();
                    List UserList = null;
                    request.setAttribute("UserList", dbutil.getUserLoginNames());
                    CustAddress custAddress = new CustAddress();
                    CustomerAccounting custAcct = new CustomerAccounting();
                    String CustomerName = "";
                    String CustomerNo = "";
        %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/util.js'></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/SearchCustomerReportBC.js'></script>
        <script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
        <script language="javascript" type="text/javascript">
            dwr.engine.setTextHtmlHandler(dwrSessionError);
            function CustomerReport(){
                document.searchCustomerSample.buttonValue.value="printingReport";
                document.searchCustomerSample.submit();
            }
            function getClientInfo(ev){
                if(event.keyCode==9 || event.keyCode==13){
                    var params = new Array();
                    params['requestFor'] = "CustAddress";
                    params['custName'] = document.searchCustomerSample.customerName.value;
                    var bindArgs = {
                        url: "<%=path%>/actions/getCustDetails.jsp",
                        error: function(type, data, evt){alert("error");},
                        mimetype: "text/json",
                        content: params
                    };
                    var req = dojo.io.bind(bindArgs);
                    dojo.event.connect(req, "load", this, "populateClientInfo");
                }
            }
            function populateClientInfo(type, data, evt) {
                if(data){
                    document.getElementById("customerName").value=data.custName;
                    if(data.custNumber){
                        document.getElementById("customerNumber").value=data.custNumber;
                    }else{
                        document.getElementById("customerNumber").value="";
                    }
                }
            }
            function fillSearchCustomerSampleList(data) {
                var trClass = "odd";
                dwr.util.removeAllRows("CustomerSampleStatement", { filter:function(tr) {
                        return (tr.id != "CustomerSamplePattern");
                    }});
                var id = 1;
                for(var i =0; i < data.length; i++) {
                    var customerStatement = data[i];
                    dwr.util.cloneNode("CustomerSamplePattern", { idSuffix:id });
                    dwr.util.setValue("tableCustomerNo" + id, customerStatement.accountNo);
                    dwr.util.setValue("tableCustomerName" + id, customerStatement.name);
                    dwr.util.setValue("tableInvoiceNumber" + id, customerStatement.invoiceNumber);
                    dwr.util.setValue("tableInvoiceDate" + id, customerStatement.transactionDate);
                    dwr.util.setValue("tableBalance" + id, customerStatement.balance);
                    dwr.util.setValue("tableAging" + id, customerStatement.aging);
                    document.getElementById("CustomerSamplePattern" + id).style.display = "block";
                    document.getElementById("CustomerSamplePattern" + id).className = trClass;
                    if(trClass == "odd") {
                        trClass = "even";
                    }else {
                        trClass = "odd";
                    }
                    id++;
                }
                document.body.style.cursor = "auto";
            }
            function searchform(){
                if(document.searchCustomerSample.customerName.value=="" && document.customerstatementform.collector.value=="0"){
                    alert("Plese enter customer Name or collector");
                    setColor(document.customerstatementform.customerName.focus());
                    return false;
                }
                var acctNum=document.searchCustomerSample.customerNumber.value;
                var collector=document.getElementById('collector').value;
                SearchCustomerReportBC.searchCustomers(acctNum,collector,fillSearchCustomerSampleList);
                //document.searchCustomerSample.buttonValue.value="search";
                //document.searchCustomerSample.submit();
            }
            function setColor(obj){
                obj.setAttribute('onfocus','this.style.backgroundColor="lightyellow"');
                obj.setAttribute('onBlur','this.style.backgroundColor="white"');
                obj.focus();
                return false;
            }
        </script>
        <script type="text/javascript">
            dojo.hostenv.setModulePrefix('utils', 'utils');
            dojo.widget.manager.registerWidgetPackage('utils');
            dojo.require("utils.AutoComplete");
            dojo.require("dojo.io.*");
            dojo.require("dojo.event.*");
            dojo.require("dojo.html.*");
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/searchCustomerSample" name="searchCustomerSample" type="com.gp.cvst.logisoft.struts.form.SearchCustomerSampleForm" scope="request">
            <html:hidden property="buttonValue"/>
            <c:if test="${fileName!=null}">
                <script type="text/javascript">
                    parent.searchCustomerSample.parent.viewReport("CustomerSample Report","${reportFileName}");
                </script>
            </c:if>
            <table width="100%"  border="0" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table width="100%"  border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" >
                            <tr class="tableHeadingNew"><td>Forwarder /Shipper /Others</td>
                                <td align="right" class="textlabelsBold"><input type="button" class="buttonStyleNew" value="Print" onclick="CustomerReport()" style="width:60px" /></td>
                            </tr>
                            <tr>
                                <td colspan="2">
                                    <table width="100%"  border="0" cellpadding="4" cellspacing="0">
                                        <tr>
                                            <td class="textlabelsBold">Customer Name</td>
                                            <td  class="textlabelsBold"><input type="text" name="customerName" id="customerName" class="textlabelsBoldForTextBox" style="width:220px" onkeydown="getClientInfo(this.value)"/>
                                                <dojo:autoComplete formId="searchCustomerSample" textboxId="customerName" action="<%=path%>/actions/getCustomerName.jsp?tabName=CUSTOMER_STATEMENT"/></td>
                                            <td class="textlabelsBold">Customer #</td>
                                            <td><html:text property="customerNumber" styleClass="textfieldstyle" value="<%=CustomerNo%>" readonly="true" style="width:220px" /></td>
                                        </tr>
                                        <tr>
                                            <td class="textlabelsBold">Collectors </td>
                                            <td><html:select property="collector" style="width:227px" > <html:optionsCollection name="UserList"/> </html:select></td>
                                            <td class="textlabelsBold"><html:radio property="allCustomer" value="yes" /> All Customers</td>
                                            <td class="textlabelsBold"><html:radio property="allCollectors" value="yes" /> All Collectors</td>
                                        </tr>
                                    </table>
                                </td>
                            </tr>
                            <tr>
                                <td align="center" colspan="2"><input type="button" class="buttonStyleNew" value="Search" onclick="searchform()" style="width:60px" /></td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr><td>
                        <br/>
                        <table width="100%" class="tableBorderNew" cellpadding="0" cellspacing="0" >
                            <tr class="tableHeadingNew"><td>Search List</td></tr>
                            <tr>
                                <td>
                                    <div id="divtablesty1" style="border: thin; overflow: scroll; width: 100%; height: 200px;">
                                        <table width="100%" border="0" cellpadding="1" cellspacing="1" class="displaytagstyleNew"
                                               style="border: thin; overflow: scroll; width: 98%; height: 20px;">
                                            <thead>
                                                <tr>
                                                    <th>Customer Name</th><th>Customer #</th><th>InvoiceNumber</th><th>InvoiceDate</th><th>Balance</th><th></th>
                                                </tr>
                                            </thead>
                                            <tbody id="CustomerSampleStatement">
                                                <tr id="CustomerSamplePattern" style="display:none;">
                                                    <td><span id="tableCustomerName"></span></td>
                                                    <td><span id="tableCustomerNo"></span></td>
                                                    <td><span id="tableInvoiceNumber"></span></td>
                                                    <td><span id="tableInvoiceDate"></span></td>
                                                    <td><span id="tableBalance"></span></td>
                                                    <td><span id="tableAging"></span></td>
                                                </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </td>
                            </tr>
                        </table>
                    </td></tr></table>
                </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html:html>
