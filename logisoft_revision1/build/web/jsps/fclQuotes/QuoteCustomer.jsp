<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cvst.logisoft.domain.CustAddress,com.gp.cong.logisoft.bc.fcl.QuotationConstants"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %> 
<%@include file="../includes/jspVariables.jsp" %>

<%
String path = request.getContextPath();
String link="";

String button="";
if(session.getAttribute("buttonValue")!=null){
button=(String)session.getAttribute("buttonValue");
}
String editPath=path+"/quoteCustomer.do";
List quotationList=null;
if(session.getAttribute("customerList")!=null){
quotationList=(List)session.getAttribute("customerList");
}

if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("QuotationClient"))
{
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getCustomer('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[7]}','${clientList[8]}','${clientList[9]}','${clientList[11]}','${clientList[12]}','${clientList[13]}');
</script>
<%
}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("QuotationCarrier")){
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getCarrier('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}');
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("lclSupplier")){
%>
<script type="text/javascript">
    val0 = '${clientList[0]}';
    val1 = '${clientList[1]}';
    val2 = '${clientList[2]}';
    val3 = '${clientList[3]}';
    val4 = '${clientList[4]}';
    val5 = '${clientList[5]}';
    val6 = '${clientList[6]}';
    val7 = '${clientList[7]}';
    val8 = '${clientList[8]}';
    val9 = '${clientList[9]}';
    val10 = '${clientList[10]}';
    val11 = '${clientList[11]}';
    val12 = '${clientList[12]}';
    parent.getThirdParties(val0,val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12);
    parent.$.fn.colorbox.close();
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("lclShipper")){
%>
<script type="text/javascript">
    val0 = '${clientList[0]}';
    val1 = '${clientList[1]}';
    val2 = '${clientList[2]}';
    val3 = '${clientList[3]}';
    val4 = '${clientList[4]}';
    val5 = '${clientList[5]}';
    val6 = '${clientList[6]}';
    val7 = '${clientList[7]}';
    val8 = '${clientList[8]}';
    val9 = '${clientList[9]}';
    val10 = '${clientList[10]}';
    val11 = '${clientList[11]}';
    val12 = '${clientList[12]}';
    parent.getThirdPartiesShipper(val0,val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12);
    parent.$.fn.colorbox.close();
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("lclForwarder")){
%>
<script type="text/javascript">
    val0 = '${clientList[0]}';
    val1 = '${clientList[1]}';
    val2 = '${clientList[2]}';
    val3 = '${clientList[3]}';
    val4 = '${clientList[4]}';
    val5 = '${clientList[5]}';
    val6 = '${clientList[6]}';
    val7 = '${clientList[7]}';
    val8 = '${clientList[8]}';
    val9 = '${clientList[9]}';
    val10 = '${clientList[10]}';
    val11 = '${clientList[11]}';
    val12 = '${clientList[12]}';
    parent.getThirdPartiesForwarder(val0,val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12);
    parent.$.fn.colorbox.close();
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("lclConsignee")){
%>
<script type="text/javascript">
    val0 = '${clientList[0]}';
    val1 = '${clientList[1]}';
    val2 = '${clientList[2]}';
    val3 = '${clientList[3]}';
    val4 = '${clientList[4]}';
    val5 = '${clientList[5]}';
    val6 = '${clientList[6]}';
    val7 = '${clientList[7]}';
    val8 = '${clientList[8]}';
    val9 = '${clientList[9]}';
    val10 = '${clientList[10]}';
    val11 = '${clientList[11]}';
    val12 = '${clientList[12]}';
    parent.getThirdPartiesConsignee(val0,val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12);
    parent.$.fn.colorbox.close();
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("lclNotify")){
%>
<script type="text/javascript">
    val0 = '${clientList[0]}';
    val1 = '${clientList[1]}';
    val2 = '${clientList[2]}';
    val3 = '${clientList[3]}';
    val4 = '${clientList[4]}';
    val5 = '${clientList[5]}';
    val6 = '${clientList[6]}';
    val7 = '${clientList[7]}';
    val8 = '${clientList[8]}';
    val9 = '${clientList[9]}';
    val10 = '${clientList[10]}';
    val11 = '${clientList[11]}';
    val12 = '${clientList[12]}';
    parent.getThirdPartiesNotify(val0,val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12);
    parent.$.fn.colorbox.close();
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("Booking")){
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getBookingCustomer('${clientList[9]}','${clientList[3]}','${clientList[0]}','${clientList[1]}',
    '${clientList[8]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[13]}',
    '${clientList[5]}','${clientList[6]}','${clientList[7]}');
</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("RoutedQuotation"))
{
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getRoutedByAgentFromPopup('${clientList[0]}','${clientList[1]}','${clientList[2]}','${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[7]}','${clientList[8]}','${clientList[9]}','${clientList[11]}','${clientList[12]}','${clientList[13]}','${clientList[13]}');
</script>
<%
}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("AccountNameShipper")){
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getBookingCustomer('${clientList[0]}','${clientList[1]}','${clientList[7]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[13]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[9]}');

</script>
<%} else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("AccountNameForwarder")){
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getBookingCustomer('${clientList[0]}','${clientList[1]}','${clientList[7]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[13]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[9]}');

</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("AccountNameThirdParty")){
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getBookingCustomer('${clientList[0]}','${clientList[1]}','${clientList[7]}','${clientList[9]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[9]}');


</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").equals("AccountNameAgent")){
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getBookingCustomer('${clientList[0]}','${clientList[1]}','${clientList[7]}','${clientList[9]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[9]}');


</script>
<%}else if(request.getAttribute("buttonValue")!=null && request.getAttribute("buttonValue").
equals("RoutedBooking"))
{
%>
<script type="text/javascript">
    parent.parent.GB_hide();
    parent.parent.getRoutedByAgentFromPopup('${clientList[0]}','${clientList[1]}','${clientList[2]}',
    '${clientList[3]}','${clientList[4]}','${clientList[5]}','${clientList[6]}','${clientList[7]}','${clientList[8]}',
    '${clientList[9]}','${clientList[10]}','${clientList[11]}','${clientList[12]}','${clientList[13]}');
</script>
<%
}%>

<html> 
    <head>
        <title>JSP for QuoteCustomerForm form</title>
    </head>
    <%@include file="../includes/baseResources.jsp" %>
    <script type="text/javascript">
        function getClose(){
            parent.parent.GB_hide();
        }
        function getGo(){
            if(document.quoteCustomerForm.customerName.value=="" && document.quoteCustomerForm.customerNo.value==""){
                alert("Please enter either Customer Name or Customer Number");
                return;
            }
            document.quoteCustomerForm.button.value="Go";
            document.quoteCustomerForm.submit();
        }
        function set(){
            document.quoteCustomerForm.customerName.focus();
        }
        function setFocus(){
            setTimeout("set()",150);
        }
    </script>
    <body class="whitebackgrnd" >
        <html:form action="/quoteCustomer" scope="request">
            <table width="100%" border="0" cellpadding="2" cellspacing="2" class="tableBorderNew">
                <tr class="tableHeadingNew"><font style="font-weight: bold">Customer Search</font></tr>
            <tr>
                <td><table>
                        <tr class="textlabelsBold">
                            <td>Customer Name</td>
                            <td><html:text property="customerName"  styleId="customerName1" 
                                       styleClass="textlabelsBoldForTextBox"></html:text></td>
                            <td>Customer No</td>
                            <td><html:text property="customerNo" styleClass="textlabelsBoldForTextBox"></html:text></td>
                            <td><input type="button" class="buttonStyleNew" value="Go" onclick="getGo()"></td>
                        </tr>
                    </table></td>
            </tr>  
        </table>

        <br style="padding-top:2px;"/>
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
            <tr class="tableHeadingNew"><font style="font-weight: bold">List of Customers</font></tr>
        <tr><td>
                <%
                        if(quotationList!=null && quotationList.size()>0){
                                int i=0; 
                %>
                <div id="divtablesty1">
                    <display:table name="<%=quotationList%>" class="displaytagstyleNew" pagesize="10"  style="width:100%" id="arInquiry" sort="list" >

                        <display:setProperty name="paging.banner.some_items_found">
                            <span class="pagebanner">
                                <font color="blue">{0}</font> Trading Partners displayed,For more Records click on page numbers.
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
                        <display:setProperty name="paging.banner.item_name" value="Trading Partners"/>
                        <display:setProperty name="paging.banner.items_name" value="Trading Partners"/>
                        <%
                        String accountName="";
                        String accountNo="";
                        String coname="";
                        CustAddress custAddress=(CustAddress)quotationList.get(i);
                                if(custAddress.getAcctName()!=null){
                                  accountName=custAddress.getAcctName();
                                }
                                if(custAddress.getAcctNo()!=null){
                                  accountNo=custAddress.getAcctNo();
                                }
                                if(custAddress.getCoName()!=null){
                                  coname=custAddress.getCoName();
                                }
                        link=editPath+"?paramId="+i+"&button="+button+"&accountNo="+accountNo;
                        %>
                        <display:column title="CustomerName" ><a href="<%=link%>" ><%=accountName%></a></display:column>
                        <display:column title="coName" ><%=coname%></display:column>
                        <display:column property="acctNo" title="CustomerNo"></display:column>
                        <display:column property="acctType" title="Account Type"></display:column>
                        <display:column title="ECI Acct No" property="eciAcctNo"></display:column>
                        <display:column property="contactName" title="Contact Name"></display:column>
                        <display:column property="address1" title="Address"></display:column>
                        <display:column property="city1" title="City"></display:column>
                        <display:column property="state" title="State"></display:column>
                        <display:column property="cr" title="CR"></display:column>
                        <display:column property="onHold" title="OnHold"></display:column>
                        <display:column property="insure" title="Insure"></display:column>
                        <display:column property="phone" title="Phone"></display:column>
                        <display:column property="fax" title="Fax"></display:column>
                        <display:column property="email1" title="Email"></display:column>

                        <%i++;%>
                    </display:table>  
                    <%}%>
                </div>
            </td></tr>
    </table>
    <html:hidden property="button"/>
    <input type="hidden" name="action" value="<%=button%>"/>
</html:form>
</body>
<script>setFocus()</script>
<script>
    function getThirdParties(val1,val2,val3,val4,val5,val6,val7,val8,val9,val10,val11,val12,val13){
        alert('hsdfgdsj')
        parent.$.fn.colorbox.close();
    }
</script>
<%@include file="../includes/baseResourcesForJS.jsp" %>

</html>

