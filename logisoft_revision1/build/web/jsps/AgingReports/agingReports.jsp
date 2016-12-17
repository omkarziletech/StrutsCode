<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ page import="com.gp.cvst.logisoft.domain.*,com.gp.cong.logisoft.beans.*,java.util.*"%>
<%@page import="com.gp.cong.common.CommonConstants"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display" %>
<%@include file="../includes/jspVariables.jsp" %>
<html:html>
    <head>
        <title> AR Aging Reports  </title>
        <%
                    DBUtil dbUtil = new DBUtil();
                    UNLocationBean unBean = new UNLocationBean();
                    String path = request.getContextPath();
                    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
                    if (path == null) {
                        path = "../..";
                    }
                    CustAddress custAddress = new CustAddress();
                    String CustomerAddress = "";
                    List searchList = null;
                    if ((List) session.getAttribute("searchList") != null) {
                        searchList = (List) session.getAttribute("searchList");
                        session.removeAttribute("searchList");
                    }
                    com.gp.cvst.logisoft.util.DBUtil dbutil = new com.gp.cvst.logisoft.util.DBUtil();
                    request.setAttribute("checkType", dbutil.checkType());
                    request.setAttribute("UserList", new com.gp.cong.logisoft.util.DBUtil().getAllUsers());
        %>
        <%@include file="../includes/baseResources.jsp" %>
        <script type='text/javascript' src="<c:url value="/dwr/engine.js" />"></script>
        <script type='text/javascript' src="<c:url value="/dwr/util.js" />"></script>
        <script type='text/javascript' src="<c:url value="/dwr/interface/TransactionDAO.js" />"></script>
        <script type='text/javascript' src="<c:url value="/dwr/interface/UploadDownloaderDWR.js" />"></script>
        <link href="<%=path%>/js/tooltip/clientStyles.css" rel="stylesheet" type="text/css" />
        <%@include file="../includes/resources.jsp" %>
        <script language="Javascript" type="text/javascript" src="<c:url value="/js/common.js" />"></script>
        <script type="text/javascript" src="<%=path%>/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="<%=path%>/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="<%=path%>/js/autocomplete.js"></script>
        <script type="text/javascript" src="<c:url value="/js/caljs/calendar.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/caljs/lang/calendar-en.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/caljs/calendar-setup.js" />"></script>
        <script type="text/javascript" src="<c:url value="/js/caljs/CalendarPopup.js" />"></script>
    </head>
    <body class="whitebackgrnd">
        <html:form action="/agingReport" name="AgingReportForm" type="com.gp.cvst.logisoft.struts.form.AgingReportForm" styleId="AgingReportForm" scope="request" >
            <html:hidden property="buttonValue"/>
            <c:if test="${!empty reportFileName}">
                <script type="text/javascript">
                    window.parent.agingReport.parent.viewReport("AR Aging Report","${reportFileName}");
                </script>
            </c:if>
            <table width="100%"  border="0" cellpadding="2" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Aging Report</td>
                    <td align="right" class="textlabelsBold">
                        <input type="button" class="buttonStyleNew" value="Print" onclick="createReport()" style="width:60px" />
                        <input type="button" class="buttonStyleNew" value="ExportToExcel" onclick="exportToExcel()" style="width:90px" />
                    </td>
                </tr>
                <tr>
                    <td colspan="2">
                        <table width="100%"  border="0" cellpadding="0" cellspacing="2">
                            <tr>
                                <td>
                                    <table cellpadding="2" cellspacing="0" border="0" class="textlabelsBold">
                                        <tr>
                                            <td  align="right">Customer Name : </td>
                                            <td>
                                                <html:text property="customerName" styleId="customerName" value="${AgingReportForm.customerName}" 
                                                           styleClass="textlabelsBoldForTextBox" style="width:150px;text-transform:uppercase;"/>
                                                <input name="custname_check" id="custname_check"  type="hidden" value="${AgingReportForm.customerName}"/>
                                                <div id="custname_choices" style="display: none" class="autocomplete"></div>
                                                <script type="text/javascript">
                                                    initAutocomplete("customerName","custname_choices","customerNumber","custname_check","<%=path%>/actions/getAccountDetails.jsp?tabName=AGING_REPORTS&from=1");
                                                </script>
                                            </td>
                                        </tr>


                                        <tr>
                                            <td align="right">Customer # : </td>
                                            <td><input type="text" name="customerNumber" id="customerNumber"
                                                       value="${AgingReportForm.customerNumber}" class="textlabelsBoldForTextBox"  style="width:150px;text-transform: uppercase;"/>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td align="right">All Customers : </td>
                                            <td> <html:checkbox property="allCustomersCheck"/> </td>
                                        </tr>

                                        <tr>
                                            <td  align="right">Show : </td>
                                            <td>
                                                <div class="field-box">
                                                    Shipper<input type="radio" name="showShipperConsignee" value="showShipper" checked="checked">
                                                    Consignee <input type="radio" name="showShipperConsignee" value="showConsignee">
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td  align="right"> Customer Range : </td>
                                            <td>
                                                <div class="field-box">
                                                    From
                                                    <html:text property="customerRangeFrom" size="2" styleClass="textlabelsBoldForTextBox"></html:text>
                                                    To
                                                    <html:text property="customerRangeTo" size="2" styleClass="textlabelsBoldForTextBox"></html:text>
                                                </div>
                                            </td>
                                        </tr>
                                        <tr>
                                            <td align="right">
                                                Cut-off Date :
                                            </td>
                                            <td>
                                                <div class="float-left">
                                                    <html:text property="dateRangeTo" readonly="true" size="10" styleId="txtcals2" styleClass="textlabelsBoldForTextBox"></html:text>
                                                </div>
                                                <div class="calendar-img">
                                                    <img src="<%=path%>/img/CalendarIco.gif" alt="cal" width="14" height="14" align="top" id="cals2" onmousedown="insertDateFromCalendar(this.id,0);" />
                                                </div>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td align="right"> Include payments regardless<br/> of payment date : </td>
                                            <td>
                                                <html:checkbox property="noPaymentDate"/>
                                            </td>
                                        </tr>
                                    </table>
                                </td>

                                <td>
                                    <table cellpadding="2" cellspacing="0" border="0" class="textlabelsBold" style="vertical-align: top">


                                        <tr>
                                            <td align="right">Report  Type : </td>
                                            <td>
                                                <div class="field-box">
                                                    Summary <html:radio property="report" value="Summary" />
                                                    Detail <html:radio property="report" value="Detail" />
                                                </div>
                                            </td>
                                        </tr>

                                        <tr>
                                            <td align="right">Number of Days Outstanding : </td>
                                            <td>
                                                <html:text property="daysOverDue" style="width:40px;"
                                                           styleClass="textlabelsBoldForTextBox" onchange="testForNumber(this);"/>days
                                            </td>
                                        </tr>

                                        <tr>
                                            <td align="right">Balance $ : </td>
                                            <td>
                                                <html:select property="checkType" styleId="checkType" styleClass="dropdown-accounting">
                                                    <html:optionsCollection name="checkType" />
                                                </html:select>
                                                <html:text property="minAmount"  styleClass="textlabelsBoldForTextBox" size="5"  onchange="testForNumber(this);"/>
                                            </td>
                                        </tr>



                                        <tr>
                                            <td  align="right">Credit : </td>
                                            <td>
                                                <div class="field-box">
                                                    <html:radio property="credit" value="Y">Yes</html:radio>
                                                    <html:radio property="credit" value="N">No</html:radio>
                                                    <html:radio property="credit" value="BOTH">Both</html:radio>
                                                </div>

                                            </td>
                                        </tr>
                                        <tr >
                                            <td align="right">Phone : </td>
                                            <td><html:text property="phone" styleClass="textlabelsBoldForTextBox" value="${AgingReportForm.phone}" style="width:150px" maxlength="13" onkeypress="getIt(this)"/></td>
                                        </tr>

                                        <tr>
                                            <td align="right">Fax : </td>
                                            <td>
                                                <html:text property="fax" styleClass="textlabelsBoldForTextBox" value="${AgingReportForm.fax}" style="width:150px" maxlength="13" onkeypress="getIt(this)" />
                                            </td>
                                        </tr>

                                        <tr>
                                            <td align="right"> address : </td>
                                            <td><html:text property="address" styleClass="textlabelsBoldForTextBox" style="width:150px;text-transform:uppercase;"/></td>
                                        </tr>

                                    </table>
                                </td>

                                <td>
                                    <table cellpadding="2" cellspacing="0" border="0" >



                                        <tr>
                                            <td>
                                                <table width="100%" cellspacing="0" cellpadding="2" border="0" >

                                                    <tr class="textlabelsBold">
                                                        <td align="right" valign="top">Collector : </td>
                                                        <td>
                                                            <html:select property="collector" styleClass="dropdown_accounting" style="width:133px">
                                                                <html:optionsCollection name="UserList" styleClass="unfixedtextfiledstyle" />
                                                            </html:select> <br/>

                                                            <html:checkbox property="allCollectors" value="on" />All <br/>
                                                            <html:radio property="agents" value="onlyAgents"></html:radio>Only Agents<br/>

                                                            <html:radio property="agents" value="agentsIncluded"></html:radio>Agents Included<br/>

                                                            <html:radio property="agents" value="agentsNotIncluded" ></html:radio>Agents NOT Included<br/>
                                                        </td>
                                                    </tr>
                                                    <tr class="textlabelsBold">
                                                        <td align="right" valign="top">Include Net Settlement Invoices : </td>
                                                        <td>
                                                            <div class="field-box">
                                                                <html:radio property="includeNetSettlement" value="yes" />Yes
                                                                <html:radio property="includeNetSettlement" value="no" />No
                                                            </div>

                                                        </td>
                                                    </tr>

                                                    <tr>
                                                        <td align="right" valign="top" class="textlabelsBold">Age Range : </td>
                                                        <td>
                                                            <table width="200px" cellpadding="3" cellspacing="0" border="0" class="textlabelsBold">
                                                                <tr>
                                                                    <td > Low</td>
                                                                    <td> Medium</td>
                                                                    <td> High</td>
                                                                    <td> High</td>
                                                                </tr>
                                                                <tr>
                                                                    <td>
                                                                        <html:text property="agingZero" styleClass="textlabelsBoldForTextBox"
                                                                                   style="width:30px" ></html:text>
                                                                        <html:text property="agingThirty" styleClass="textlabelsBoldForTextBox"
                                                                                   style="width:30px" ></html:text>
                                                                    </td>


                                                                    <td>
                                                                        <html:text property="greaterThanThirty" styleClass="textlabelsBoldForTextBox"
                                                                                   style="width:30px" ></html:text>
                                                                        <html:text property="agingSixty" styleClass="textlabelsBoldForTextBox"
                                                                                   style="width:30px" ></html:text>
                                                                    </td>

                                                                    <td>
                                                                        <html:text property="greaterThanSixty" styleClass="textlabelsBoldForTextBox"
                                                                                   style="width:30px"/>
                                                                        <html:text property="agingNinty" styleClass="textlabelsBoldForTextBox"
                                                                                   style="width:30px"/>

                                                                    </td>


                                                                    <td>
                                                                        <html:text property="greaterThanNinty" styleClass="textlabelsBoldForTextBox" style="width:30px"/>
                                                                    </td>
                                                                </tr>
                                                            </table>
                                                        </td>
                                                    </tr>

                                                </table>
                                            </td>
                                        </tr>

                                    </table>

                                </td>


                            </tr>
                        </table>

                    </td>
                </tr>
            </table>
        </html:form>
        <script type="text/javascript">
            dwr.engine.setTextHtmlHandler(dwrSessionError);
            function createReport(){
                if(document.AgingReportForm.dateRangeTo.value!=""){
                    if(document.AgingReportForm.allCustomersCheck.checked){
                        alert("Aging Report for All Customers");
		   document.AgingReportForm.allCustomersCheck.value="on";
                    }else{
                        if(document.AgingReportForm.customerName.value==""){
                            alert("Please enter Customer name");
                            return true;
                        }
                        if(document.AgingReportForm.customerNumber.value==""){
                            alert("Please enter Customer Number");
                            return true;
                        }
                        document.AgingReportForm.allCustomersCheck.value="off";
                    }
                    document.AgingReportForm.buttonValue.value="createReport";
                    document.AgingReportForm.submit();
                }else{
                    alert("Please Enter Cut-Off Date");
                    return false;
                }
            }
            function exportToExcel(){
                if(document.AgingReportForm.dateRangeTo.value!=""){
                    if(document.AgingReportForm.allCustomersCheck.checked){
                        alert("Aging Report for All Customers");
		   document.AgingReportForm.allCustomersCheck.value="on";
                    }else{
                        if(document.AgingReportForm.customerName.value==""){
                            alert("Please enter Customer name");
                            return true;
                        }
                        if(document.AgingReportForm.customerNumber.value==""){
                            alert("Please enter Customer Number");
                            return true;
                        }
                        document.AgingReportForm.allCustomersCheck.value="off";
                    }
                    document.AgingReportForm.buttonValue.value="exportToExcel";
                    document.AgingReportForm.submit();
                }else{
                    alert("Please Enter Cut-Off Date");
                    return false;
                }
            }
            function testForNumber(obj){
                obj.value=obj.value.replace(/,/g,'');
                if(isNaN(obj.value)){
                    alert("Please enter Number");
                    obj.value="";
                    obj.focus();
                }
            }
        </script>
    </body>
</html:html>