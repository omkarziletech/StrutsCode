<%@ page language="java" pageEncoding="ISO-8859-1" import="com.gp.cong.logisoft.util.DBUtil,java.util.*,com.gp.cong.logisoft.domain.CustomerContact,com.gp.cong.logisoft.bc.notes.NotesConstants"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../Tradingpartnermaintainance/tradingPartnerSelectList.jsp"%>
<%    String path = request.getContextPath();
    String clientAcctNo = null, from = null, clientAcctName = null;
//----requests from customerAddress page of Quotes----
    if (request.getAttribute("customerAcctNoFromQuotes") != null) {
        clientAcctNo = (String) request.getAttribute("customerAcctNoFromQuotes");
        request.setAttribute("clientAcctNo", clientAcctNo);
        from = "QuotesContactLookUp";
    }
    if (request.getAttribute("customerAcctNameFromQuotes") != null) {
        clientAcctName = (String) request.getAttribute("customerAcctNameFromQuotes");
    }
//----ends------
%>
<html>
    <head>
        <%@include file="../includes/init.jsp"%>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../fragment/formSerialize.jspf" %>
        <script language="javascript" src="<%=path%>/js/common.js" ></script>
        <script language="javascript" src="<%=path%>/js/isValidEmail.js" ></script>
        <link type="text/css" rel="stylesheet" media="screen" href="<%=path%>/jsps/LCL/js/colorbox/colorbox.css" />
        <script type="text/javascript" src="<%=path%>/jsps/LCL/js/colorbox/jquery.colorbox.js"></script>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="/taglib.jsp"%>
    </head>

    <body class="whitebackgrnd">
        <html:form action="/tradingPartner" name="tradingPartnerForm" styleId="contactConfig" 
                   type="com.gp.cong.logisoft.struts.form.TradingPartnerForm" scope="request">
            <section id='addReports2'> 
                <table width='100%' border='0' class="tableBorderNew" >
                    <tr>
                        <td colspan='10'  class='textlabelsBold tableHeadingNew' 
                            style='font-size:14px;'>
                            Enter Contact Reports
                        </td>
                    </tr>
                    <tr class='textlabelsBold'>
                        <td>Contact Name</td>
                        <td>
                            <select id='contactname'>
                                <option value='0'>Select Contact</option>
                            </select>
                        </td>
                        <td>Report Name</td>
                        <td>
                            <select id='report'>
                                <option value='0'>Report Name</option>
                            </select>
                        </td>
                        <td>Report Option</td>
                        <td>
                            <select id='contactname'>
                                <option value='0'>Report Option</option>
                            </select>
                        </td>
                        <td>Frequency Min</td>
                        <td>
                            <input type='text' size='3' id='freqMin' name='freqMin'/>
                        </td>
                        <td>Disabled</td>
                        <td>
                            <input type='radio' id='freqDisabled' name='freqDisabled' value='Yes'/>YES
                            <input type='radio' id='freqDisabled' name='freqDisabled' value='No'/>NO
                        </td>
                    </tr>
                    <tr><td colspan='10' style='padding-bottom:1em;'></td></tr>
                    <tr class='textlabelsBold'>
                        <td>Email</td>
                        <td>
                            <input type='text' size='17' id='email' name='email' class='bold'/>
                        </td>
                        <td>Frequency Hour</td>
                        <td>
                            <input type='text' size='3' id='freqHour' name='freqHour'/>
                        </td>
                        <td>Frequency Dom</td>
                        <td>
                            <input type='text' size='3' id='freqDom' name='freqDom'/>
                        </td>
                        <td>Frequency Month</td>
                        <td>
                            <input type='text' size='3' id='freqMonth' name='freqMonth'/>
                        </td>
                        <td>Frequency Dow</td>
                        <td>
                            <input type='text' size='3' id='freqDow' name='freqDow'/>
                        </td>
                    </tr>
                    <tr><td colspan='10' style='padding-bottom:1em;'></td></tr>
                    <tr>
                        <td style='padding-bottom:2em;' colspan="4"></td>
                        <td colspan="6">
                            <input type='button' value='Save' class='buttonStyleNew' onclick='saveContactReport()'/>
                            <input type='button' value='Cancel' class='buttonStyleNew' onclick='closeSection()'/>
                        </td>
                    </tr>
                </table>
            </section>

            <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew" colspan="2">
                    <td>
                        Contact Details
                    </td>
                    <td align="right">
                        <input type="button" class="buttonStyleNew" value="Add" onclick='toggleReports()'/>
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <div id="divtablesty1" class="scrolldisplaytable">
                            <table width="100%" border="0" cellpadding="0" cellspacing="0">
                                <%int i = 0;%>
                                <display:table name="${TRADINGPARTNER.customerContact}" class="displaytagstyleNew" 
                                id="contactConfigTable" pagesize="<%=pageSize%>" defaultsort="1" defaultorder="ascending">

                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font> Contact Details displayed,For more Customer Contacts click on Page Numbers.

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
                                    <display:column title="FirstName" property="firstName"/>
                                    <display:column title="LastName" property="lastName"/>
                                    <display:column title="Position" property="position"/>
                                    <display:column title="Phone Number" property="phone"/>
                                    <display:column title="Extension" property="extension"/>
                                    <display:column title="Fax" property="fax"/>
                                    <display:column title="Email" property="email"/>
                                    <display:column title="A" property="codea.code" style="width:30px"/>
                                    <display:column title="B" property="codeb.code" style="width:30px"/>
                                    <display:column title="C" property="codec.code" style="width:30px"/>
                                    <display:column title="D" property="coded.code" style="width:30px"/>
                                    <display:column title="E" property="codee.code" style="width:30px"/>
                                    <display:column title="F" property="codef.code" style="width:30px"/>
                                    <display:column title="G" property="codeg.code" style="width:30px"/>
                                    <display:column title="H" property="codeh.code" style="width:30px"/>
                                    <display:column title="I" property="codei.code" style="width:30px"/>
                                    <display:column title="J" property="codej.code" style="width:30px"/>${contactConfigTable.codej.code}
                                    <display:column title="K" property="codek.code" style="width:30px"/>
                                </display:table>
                            </table>
                        </div>
                    </td>
                </tr>
            </table>
        </html:form> 
        <script language="javascript">
            jQuery(document).ready(function () {
                jQuery("#addReports").hide();
            });

            function toggleReports() {
                jQuery("#addReports").toggle();
            }

            function saveContactReport() {
                // post mothod for contact reports.
            }
        </script>