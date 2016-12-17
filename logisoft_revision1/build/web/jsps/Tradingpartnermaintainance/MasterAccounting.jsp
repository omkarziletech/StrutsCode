<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,com.gp.cong.logisoft.domain.CustomerAccounting,com.gp.cong.logisoft.domain.GenericCode,java.util.*,com.gp.cong.logisoft.beans.customerBean,com.gp.cong.logisoft.domain.GeneralInformation,com.gp.cong.logisoft.domain.User"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@include file="../includes/jspVariables.jsp" %>
<%
        String path = request.getContextPath();

        DBUtil dbUtil = new DBUtil();
        CustomerAccounting accounting = new CustomerAccounting();
        GeneralInformation generalInformation = new GeneralInformation();

        String arcode = "0";
        List list = new ArrayList();//----------LISTS USED FOR DISPLAY COLUMNS------
        List list1 = new ArrayList();
        List cList = new ArrayList();

        accounting.setHoldList("off");
        accounting.setExtendCredit("off");
        accounting.setIncludeagent("off");
        accounting.setCreditbalance("off");
        accounting.setCreditinvoice("off");
        generalInformation.setInsure("off");
        String statements = "0";
        customerBean customerbean = new customerBean();
        if (session.getAttribute("customerbean") != null) {
            customerbean = (customerBean) session.getAttribute("customerbean");
            customerbean.setHoldList(customerbean.getHoldList());
            customerbean.setExtendCredit(customerbean.getExtendCredit());
            customerbean.setCompanyMaster(customerbean.getCompanyMaster());
            customerbean.setAddressMaster(customerbean.getAddressMaster());
            customerbean.setIncludeagent(customerbean.getIncludeagent());
            customerbean.setCreditbalance(customerbean.getCreditbalance());
            customerbean.setCreditinvoice(customerbean.getCreditinvoice());
        }
        request.setAttribute("customerbean", customerbean);
        request.setAttribute("countrylist", dbUtil.getGenericCodeList(11, "yes", "Select Country"));
        String countryId = "";
        String cityId = "";
        String state = "";
        String paycountryId = "";
        String paycityId = "";
        String paystate = "";
        GenericCode genericCode = null;
        GenericCode genericCode1 = null;
        String creditRate = "0";
        List cities = new ArrayList();
        List paycities = new ArrayList();
        String creditLimit = "";
        String phone = "";
        String fax = "";


        /*if(session.getAttribute("CPlist")!=null)
        {
        accounting=(CustomerAccounting)session.getAttribute("CPlist");

        }*/

        String schedule = "";
        String creditstatus = "";
        if (session.getAttribute("masteraccounting") != null) {
            accounting = (CustomerAccounting) session.getAttribute("masteraccounting");
            cList.add(accounting);//---------LIST TO DISPLAY CONTACT INFORMATION-------


            if (accounting.getAddress1() != null) {

                list.add(accounting);//-------LIST DISPLAYING ADDRESS INFORMATION----
            }

            if (accounting.getPayAddress1() != null) {

                list1.add(accounting);
            }

            if (accounting != null && accounting.getPayCuntry() != null && accounting.getPayCuntry().getCodedesc() != null) {
                paycountryId = accounting.getPayCuntry().getCodedesc();
            }


            if (accounting.getArPhone() != null) {
                phone = dbUtil.appendstring(accounting.getArPhone());
            }
            if (accounting.getArFax() != null) {
                fax = dbUtil.appendstring(accounting.getArFax());
            }
            if (accounting.getCreditLimit() != null) {
                creditLimit = String.valueOf(accounting.getCreditLimit());
            }
            if (accounting != null && accounting.getArcode() != null && accounting.getArcode().getId() != null) {
                arcode = accounting.getArcode().getId().toString();
            }
            if (accounting != null && accounting.getStatements() != null && accounting.getStatements().getId() != null) {
                statements = accounting.getStatements().getId().toString();
            }


            if (accounting != null && accounting.getCuntry() != null && accounting.getCuntry().getCodedesc() != null) {
                countryId = accounting.getCuntry().getCodedesc();
            }
            if (accounting != null && accounting.getCity2() != null) {
                cityId = accounting.getCity2();
            }
            if (accounting != null && accounting.getState() != null) {
                state = accounting.getState();
            }
            if (accounting != null && accounting.getCreditRate() != null && accounting.getCreditRate().getId() != null) {
                creditRate = accounting.getCreditRate().getId().toString();
            }

            if (accounting != null && accounting.getPayCuntry() != null && accounting.getPayCuntry().getCodedesc() != null) {
                paycountryId = accounting.getPayCuntry().getCodedesc();
            }
            if (accounting != null && accounting.getPaycity2() != null) {
                paycityId = accounting.getPaycity2();
            }
            if (accounting != null && accounting.getPayState() != null) {
                paystate = accounting.getPayState();
            }

            if (accounting != null && accounting.getSchedulestmt() != null) {
                schedule = accounting.getSchedulestmt();
            }

            if (accounting != null && accounting.getCreditStatus() != null) {
                creditstatus = accounting.getCreditStatus().getId().toString();
            }
        }
        request.setAttribute("creditRateList", dbUtil.getGenericCodeList(29, "yes", "Select Credit Terms"));
        request.setAttribute("statements", dbUtil.getGenericCodeList(30, "yes", "Select Statements"));
//request.setAttribute("arcodelist",dbUtil.getUserList(24,"no","Select AR Contact Code"));
        request.setAttribute("arcodelist", dbUtil.getGenericCodeList(44, "Yes", "Select Collector Code"));
        request.setAttribute("creditStatusList", dbUtil.getGenericCodeList(43, "yes", "Select Credit Status"));
        request.setAttribute("Schedulelist", dbUtil.getScheduleList());

        User user = new User();
        if (session.getAttribute("loginuser") != null) {
            user = (User) session.getAttribute("loginuser");
        }

        boolean hold = true;
        if (user.getLoginName().equals("Mitch") || user.getLoginName().equals("Rick")) {
            hold = false;
        }

        if (request.getAttribute("openwindow") != null) {
%>
<script type="text/javascript">
    window.open("<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button=checked","", "width=700,height=360");
    //window.moveTo(200,180);
</script>
<%
        }
        if (request.getParameter("close") != null) {
%>
<script type="text/javascript">
    window.open("<%=path%>/jsps/Tradingpartnermaintainance/contactPopup.jsp?","width=700,height=400");
    // window.moveTo(200,180);
</script>
<%
        }
%>  

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base >

        <title>JSP for CustomerForm form</title>

        <%@include file="../includes/baseResources.jsp" %>

        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <script type="text/javascript">
            function selectcity()
            {
                if(document.masterAccountingForm.country.value=="0")
                {
                    alert("Please enter the country");
                    return;
                }
                document.masterAccountingForm.buttonValue.value="selectcity";
                document.masterAccountingForm.submit();
            }
            function selectstate()
            {
                if(document.masterAccountingForm.city.value=="0")
                {
                    alert("Please enter the city");
                    return;
                }
                document.masterAccountingForm.buttonValue.value="selectstate";
                document.masterAccountingForm.submit();
            }
            function selectpaycity()
            {
                if(document.masterAccountingForm.ffcountry.value=="0")
                {
                    alert("Please enter the country");
                    return;
                }
                document.masterAccountingForm.buttonValue.value="selectcity";
                document.masterAccountingForm.submit();
            }
            function selectpaystate()
            {
                if(document.masterAccountingForm.ffcity.value=="0")
                {
                    alert("Please enter the city");
                    return;
                }
                document.masterAccountingForm.buttonValue.value="selectstate";
                document.masterAccountingForm.submit();
            }


            function popup1(mylink, windowname)
            {
                if (!window.focus)return true;
                var href;
                if (typeof(mylink) == 'string')
                    href=mylink;
                else
                    href=mylink.href;
                mywindow=window.open(href, windowname, 'width=400,height=250,scrollbars=yes');
                mywindow.moveTo(200,180);

                document.masterAccountingForm.submit();
                return false;
            }

            function toUppercase(obj)
            {
                obj.value = obj.value.toUpperCase();
            }

            function limitText(limitField, limitCount, limitNum) {
                limitField.value = limitField.value.toUpperCase();
                if (limitField.value.length > limitNum) {
                    limitField.value = limitField.value.substring(0, limitNum);
                } else {
                    limitCount.value = limitNum - limitField.value.length;
                }

            }

            function zipcode(obj)
            {
                if(document.masterAccountingForm.country.value=="" && document.masterAccountingForm.country.value!="United States")
                {

                    if((document.masterAccountingForm.zipCode.value.length>5) || IsNumeric(document.masterAccountingForm.zipCode.value.replace(/ /g,''))==false)
                    {
                        alert("please enter the only 5 digits and numerics only");
                        document.masterAccountingForm.zipCode.value="";
                        document.masterAccountingForm.zipCode.focus();
                    }
                }
                else{
                    getzip(obj);
                }
            }

            function ffzipCode1(obj)
            {
                if(document.masterAccountingForm.ffcountry.value=="" && document.masterAccountingForm.ffcountry.value!="United States")
                {

                    if((document.masterAccountingForm.ffzipCode.value.length>5) || IsNumeric(document.masterAccountingForm.ffzipCode.value.replace(/ /g,''))==false)
                    {
                        alert("please enter the only 5 digits and numerics only");
                        document.masterAccountingForm.ffzipCode.value="";
                        document.masterAccountingForm.ffzipCode.focus();
                    }
                }
                else{
                    getzip(obj);
                }
            }

            function setcreditterms()
            {
                (document.masterAccountingForm.creditLimit.value == 0)?document.masterAccountingForm.creditRate.selectedIndex=1:document.masterAccountingForm.creditLimit.value > 0?document.masterAccountingForm.creditRate.selectedIndex=3:document.masterAccountingForm.creditRate.selectedIndex=0;
            }

            function setCreditStatus1()
            {
                document.masterAccountingForm.suspendCredit.checked? document.masterAccountingForm.creditStatus.selectedIndex=4: document.masterAccountingForm.legal.checked?document.masterAccountingForm.creditStatus.selectedIndex=5:document.masterAccountingForm.creditStatus.selectedIndex=0;
            }

            function setCreditStatus2()
            {
                document.masterAccountingForm.legal.checked? document.masterAccountingForm.creditStatus.selectedIndex=5: document.masterAccountingForm.suspendCredit.checked?document.masterAccountingForm.creditStatus.selectedIndex=4:document.masterAccountingForm.creditStatus.selectedIndex=0;
            }

            function popup()
            {
                document.masterAccountingForm.submit();
                window.open('<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'masterEcoaddress',"","width=700,height=360");

            }

            function popup1()
            {
                document.masterAccountingForm.submit();
                window.open('<%=path%>/jsps/Tradingpartnermaintainance/contactPopup.jsp?button='+'mcont',"","width=700,height=400");

            }
            function popup2()
            {
                document.masterAccountingForm.submit();
                window.open('<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'masterFfaddress',"","width=700,height=360");

            }
        </script>
        <%@include file="../includes/resources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/masterAccounting" name="masterAccountingForm"
                   type="com.gp.cong.logisoft.struts.form.MasterAccountingForm" scope="request">
            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew" colspan="2">Accounts Receivable</tr>
                <tr>
                    <td  width="50%">
                        <table width="100%" border="0" cellspacing="0" cellpadding="2">
                            <tr>
                                <td><span class="style2">&nbsp;<bean:message key="form.customerForm.statements" /></span></td>
                                <td class="style2">Schedule For Statements</td>
                            </tr>
                            <tr>
                                <td>&nbsp;<html:select property="statements" styleClass="selectboxstyle" value="<%=statements%>">
                                        <html:optionsCollection name="statements"/>
                                </html:select></td>
                                <td><html:select property="schedulestmt" styleClass="selectboxstyle" value="<%=schedule%>">
                                        <html:optionsCollection name="Schedulelist"/>
                                </html:select></td>
                            </tr>
                            <tr>
                                <td class="style2">&nbsp;Credit Status</td>
                                <td class="style2">Collector Code</td>
                            </tr>
                            <tr>
                                <td>&nbsp;<html:select property="creditStatus" styleClass="selectboxstyle" value="<%=creditstatus%>">
                                        <html:optionsCollection name="creditStatusList"/>
                                    </html:select>
                                </td>
                                <td><html:select property="arCode" styleClass="dropdownboxStyle" value="<%=arcode%>">
                                        <html:optionsCollection name="arcodelist"/>
                                    </html:select>
                                </td>
                            </tr>
                            <tr>
                                <td><span class="style2">&nbsp;Account Receivable<br/>&nbsp;&nbsp;&nbsp;Comment Line</span></td>

                            </tr>
                            <tr>
                                <td  class="style2">&nbsp;<html:textarea property="acctReceive" value="<%=accounting.getComment()%>" cols="15" rows="2" onkeyup="toUppercase(this)"/></td>
                            </tr>

                    </table></td>
                    <td>&nbsp;</td>

                    <td valign="top" width="50%">
                        <table border="0" cellspacing="1">
                            <tr>
                                <td class="style2" height="21"><bean:message key="form.customerForm.creditlimit" /></td>
                                <td class="style2">&nbsp;Credit Terms</td>
                                <td class="style2">&nbsp;Include Agents</td>
                            </tr>
                            <tr>
                                <td class="style2"><html:text property="creditLimit" value="<%=creditLimit%>" onkeyup="toUppercase(this)"  onblur="setcreditterms()" maxlength="5" onkeypress="return checkIts(event)" /></td>
                                <td>&nbsp;<html:select property="creditRate" styleClass="selectboxstyle" value="<%=creditRate%>">
                                        <html:optionsCollection name="creditRateList"/>
                                    </html:select>
                                </td>
                                <td align="center">&nbsp;<html:checkbox property="includeagent" name="customerbean" ></html:checkbox></td>
                            </tr>
                        </table>
                        <table width="20%" border="0" style="padding:right:20px;" >
                            <tr class="tableHeadingNew" colspan="2" align="center">&nbsp;Send Statements With</tr>
                            <tr>
                                <td class="style2" align="left">Credit balance</td>
                                <td><html:checkbox property="creditbalance" name="customerbean"></html:checkbox></td>
                                <td class="style2" align="right">&nbsp;&nbsp;Credit Invoice</td>
                                <td><html:checkbox property="creditinvoice" name="customerbean"></html:checkbox></td>
                            </tr>
                        </table>

                </td></tr>
            </table>

            <table>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
                <tr class="tableHeadingNew" colspan="2">Contact Details</tr>
                <tr>
                    <td>
                        <table border="0" width="100%">
                            <tr>
                                <td align="right">
                                    <input type="button" class="buttonStyleNew" value="Add"  onclick="return GB_show('Contact', '<%=path%>/jsps/Tradingpartnermaintainance/contactPopup.jsp?button='+'mcont',150,600)"/>
                                </td>
                            </tr>

                            <% int j = 0;%>
                            <display:table  name="<%=cList%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" >
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
                                <display:column title="Contact Name" property="contact"></display:column>
                                <display:column title="Phone" property="arPhone"></display:column>
                                <display:column title="Fax" property="arFax"></display:column>
                                <display:column title="Email" property="acctRecEmail"></display:column>
                                <%j++;%>
                            </display:table>
                        </table>
                    </td>
                </tr>
            </table>

            <table>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <table width="100%" border="0" class="tableBorderNew">
                <tr class="tableHeadingNew" colspan="2">Customer Invoice Address</tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0">
                            <tr>
                                <td align="right" width="100%">
                                    <input type="button" class="buttonStyleNew" value="Add"  onclick="return GB_show('Customer', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'masterEcoaddress',250,600)"/>
                                </td>
                            </tr>

                            <% int i = 0;%>
                            <display:table  name="<%=list%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" >
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
                                <display:column title="C/O Name" property="company" ></display:column>
                                <display:column property="address1" title="Address1"/>
                                <display:column title="City" property="city2"></display:column>
                                <display:column title="State" property="state"></display:column>
                                <display:column title="Country"><%=accounting.getCuntry().getCodedesc()%></display:column>
                                <%i++;%>
                            </display:table>

                        </table>
                    </td>
                </tr>
            </table>

            <table>
                <tr>
                    <td>&nbsp;</td>
                </tr>
            </table>

            <table width="100%" border="0" cellspacing="0" cellpadding="0" class="tableBorderNew">
                <tr class="tableHeadingNew">F.F Payment Address</tr>
                <tr>
                    <td>
                        <table width="100%" border="0" cellspacing="0" cellpadding="0" >
                            <tr>
                                <td align="right" width="100%">
                                    <input type="button" class="buttonStyleNew" value="Add"
                                           onclick="return GB_show('Address', '<%=path%>/jsps/Tradingpartnermaintainance/AddressPopUp.jsp?button='+'masterFfaddress',250,600)" />
                                </td>
                            </tr>

                            <% int k = 0;
                            %>
                            <display:table  name="<%=list1%>" pagesize="<%=pageSize%>" class="displaytagstyle" id="customertable" sort="list" >
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

                                <display:column title="C/O Name" property="payCompany" ></display:column>
                                <display:column  title="Address1" property="payAddress1"/>
                                <display:column title="ffcity" property="paycity2"></display:column>
                                <display:column title="ffstate" property="payState"></display:column>
                                <display:column title="ffcountry" ><%=accounting.getPayCuntry().getCodedesc()%></display:column>
                                <%k++;%>
                            </display:table>

                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>

