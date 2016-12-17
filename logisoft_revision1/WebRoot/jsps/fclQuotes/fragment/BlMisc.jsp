<%@ page language="java" import="java.util.*,com.gp.cong.logisoft.bc.notes.NotesConstants,com.logiware.hibernate.domain.ArRedInvoice,java.util.Iterator,java.text.NumberFormat,java.text.SimpleDateFormat,java.util.Date" pageEncoding="ISO-8859-1"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/tlds/string.tld" prefix="string"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn"%>
<%@ include file="/jsps/includes/jspVariables.jsp"%>
<%@include file="/jsps/includes/baseResources.jsp"%>

<%
    String path = request.getContextPath();
    pageContext.setAttribute("newLine", "\n");
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>My JSP 'BlMisc.jsp' starting page</title>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">    
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <style type="text/css">
            #docListDiv {
                position: fixed;
                _position: absolute;
                z-index: 99;
                border-style:solid solid solid solid;
                background-color: #FFFFFF;
                left: 0;
                right: 0;
                top: 10%;
            }
        </style>
        <script type="text/javascript" src="${path}/dwr/engine.js"></script>
        <script type="text/javascript" src="${path}/dwr/util.js"></script>
        <script type='text/javascript' src='/logisoft/dwr/interface/ProcessInfoBC.js'></script>
        <script type="text/javascript" src="${path}/dwr/interface/DwrUtil.js"></script>
        <script type='text/javascript' src='${path}/dwr/interface/QuoteDwrBC.js'></script>
        <script type='text/javascript' src='${path}/dwr/interface/FclBlBC.js'></script>
        <script type="text/javascript" src="${path}/js/common.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar.js" ></script>
        <script type="text/javascript" src="${path}/js/caljs/lang/calendar-en.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/calendar-setup.js"></script>
        <script type="text/javascript" src="${path}/js/caljs/CalendarPopup.js"></script>
        <script type="text/javascript" src="${path}/js/prototype/prototype.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/effects.js"></script>
        <script type="text/javascript" src="${path}/js/script.aculo.us/controls.js"></script>
        <script type="text/javascript" src="${path}/js/autocomplete.js"></script>
        <script type="text/javascript" src="${path}/js/Accounting/AccountsReceivable/ARRedInvoice.js"></script>
    </head>
     <div id="newProgressBar" class="progressBar" style="position: absolute;z-index: 100;left:35% ;top: 40%;right: 50%;bottom: 60%;display: none;">
        <p class="progressBarHeader" style="width: 100%;padding-left:45px;"><b>Processing......Please Wait</b></p>

        <form style="text-align:center;padding-right:4px;padding-bottom: 4px;">
            <input type="image" src="/logisoft/img/icons/newprogress_bar.gif" >
        </form>
    </div>
    <body class="whitebackgrnd">
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
        <div id="ConfirmYesOrNo" class="alert">
            <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
            <p id="innerText2" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
            <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
                <input type="button"  class="buttonStyleForAlert" value="No"
                       onclick="confirmNo()"  style="float: right">
                <input type="button"  class="buttonStyleForAlert" value="Yes"
                       onclick="confirmYes()" >
            </form>
        </div>
        <div id="commentDiv"   class="comments">
			<table border="1" id="commentTableInfo">
                            <tbody border="0"></tbody>
			</table>
        </div>
        <html:form action="/arRedInvoice" name="arRedInvoiceForm"
                   type="com.logiware.form.ARRedInvoiceForm" scope="request">
            <html:hidden property="action"/>
            <c:choose>
                <c:when test="${not empty param.importFlag}">
                    <html:hidden property="fileType" value="${param.importFlag}"/>
                </c:when>
                <c:otherwise>
                    <html:hidden property="fileType"/>
                </c:otherwise>
            </c:choose>
            <div id="cover"></div>
            <table cellpadding="1" cellspacing="7" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew"><font style="font-weight: bold">Search Criteria</font> </tr>
                <tr class="textlabelsBold">
                    <td>Invoices</td>
                    <td>
                        <html:select property="invoices" style="width:90px"  styleClass="dropdown_accounting">
                            <html:option value="">Select</html:option>
                            <html:option value="Posted">Posted</html:option>
                            <html:option value="UnPosted">Un Posted</html:option>
                        </html:select>
                    </td>
                    <td>Order By</td>
                    <td>
                        <html:select property="orderBy" style="width:90px"  styleClass="dropdown_accounting">
                            <html:option value="">Select</html:option>
                            <html:option value="orderByNo">Invoice No</html:option>
                            <html:option value="orderByDate">Date</html:option>
                        </html:select>
                    </td>
                    <td>From Date<br></td>
                    <td>
                        <fmt:formatDate pattern="MM/dd/yyyy" var="date" value="${arRedInvoice.date}"/>
                        <html:text property="invoiceStartdate"  value="${arRedInvoice.date}" onchange="validateDate(this)" styleId="txtcalfrom" size="10" styleClass="textlabelsBoldForTextBox float-left"/>
                        <img src="${path}/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calfrom" class="calendar-img"
                             onmousedown="insertDateFromCalendar(this.id,0);" />
                    </td>
                    <td valign="top">To Date<br></td>
                    <td>
                        <fmt:formatDate pattern="MM/dd/yyyy" var="date" value="${arRedInvoice.date}"/>
                        <html:text property="invoiceEnddate"  value="${arRedInvoice.date}" onchange="validateDate(this)" styleId="txtcalto" size="10" styleClass="textlabelsBoldForTextBox float-left"/>
                        <img src="${path}/img/CalendarIco.gif" alt="cal" width="16" height="16" align="middle" id="calto" class="calendar-img"
                             onmousedown="insertDateFromCalendar(this.id,0);" />
                    </td>
                </tr>
                <tr class="textlabelsBold">
                    <td valign="top">Invoice No</td>
                    <td><html:text property="invoiceNumber" styleId="invoiceNumber" value="${arRedInvoice.invoiceNumber}" styleClass="textlabelsBoldForTextBox"/></td>
                        <td valign="top">File No</td>
                        <td><html:text property="fileNo" styleId="fileNo" value="${arRedInvoice.fileNo}" styleClass="textlabelsBoldForTextBox"/></td>
                        <td valign="top">Customer Name</td>
                    <td >
                        <input name="cusName" id="customerName" value="${arRedInvoiceform.cusName}"
                               style="text-transform:uppercase;" class="textlabelsBoldForTextBox"/>
                        <input name="custname_check" id="custname_check"  type="hidden" value="${arRedInvoiceform.cusName}">
                        <input name="accountNumber" id="custNumber"  type="hidden">
                        <div id="custname_choices" style="display: none" class="autocomplete"></div>
                        <script type="text/javascript">
                            initAutocompleteWithFormClear("customerName","custname_choices","custNumber","custname_check",
                                                    "${path}/actions/tradingPartner.jsp?tabName=QUOTE&from=0","","");
                        </script>
                    </td>
                </tr>
                <tr>
                    <td align="center" colspan="8"><input value="Search" type="button"  onclick="invoicePoolSearch();" class="buttonStyleNew"/></td>
                </tr>
            </table>
                        <br>
         <c:if test="${!empty invoicePoolList}">
<%--            <table cellpadding="0" cellspacing="0" class="tableBorderNew" width="100%">
                <tr class="tableHeadingNew">Invoice Pool List</tr>
                <tr>
                    <td colspan="2">
                        <div id="disputedBlDiv">
                                <display:table name="${invoicePoolList}" pagesize="50" id="invoicePoolList"
                                               class="displaytagStyle" style="width:100%" requestURI="/arRedInvoice.do">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner">
                                            <font color="blue">{0}</font>
                                            Invoice Pool List displayed,For more Records click on page numbers.</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner">One {0} displayed. Page Number</span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner">{0} {1} displayed, Page Number</span>
                                    </display:setProperty>
                                        <display:setProperty name="basic.msg.empty_list">
                                            <span class="pagebanner">No Records Found.</span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.placement" value="bottom"/>
                                        <display:setProperty name="paging.banner.items_name" value="Invoice Pool"/>
                                        <display:column property="invoiceNumber" title="Invoice Number"/>
                                        <display:column title="File No">
                                            <span style="color:blue;cursor: pointer;text-decoration: underline" onclick="checkLockAndNavigatToBl('${invoicePoolList.fileNo}','blId=${invoicePoolList.bol}&bookingId=${invoicePoolList.bookingNo}&quoteId=${invoicePoolList.quuoteNo}&fileNumber=${invoicePoolList.fileNo}','${invoicePoolList.moduleId}','${invoicePoolList.itemId}','${invoicePoolList.selectedMenu}')">
                                                ${invoicePoolList.fileNo}
                                            </span>
                                        </display:column>
                                        <display:column  title="Created Date">
                                            <fmt:formatDate var="createdDate" pattern="dd-MM-yyyy" value="${invoicePoolList.date}"/>
                                            ${createdDate}
                                        </display:column>
                                        <display:column  title="Posted Date">
                                            <fmt:formatDate var="postDate" pattern="dd-MM-yyyy" value="${invoicePoolList.postedDate}"/>
                                            ${postDate}
                                        </display:column>
                                        <display:column  title="Dollar Amount">
                                            <fmt:formatNumber  var="dollerAmount" pattern="###,###,##0.00" value="${invoicePoolList.invoiceAmount}"/>
                                            ${dollerAmount}
                                        </display:column>
                                        <display:column property="customerName" title="Bill To Party"/>
                                        <display:column property="invoiceBy" title="User"/>
                                    <display:column title="Description" >
                                        <c:out value="${fn:substring(invoicePoolList.notes,0,fn:indexOf(invoicePoolList.notes,newLine))}"/>
                                        </display:column>
                                        <display:column title="Status">
                                            <c:choose>
                                                <c:when test="${invoicePoolList.status=='AR'}">
                                                    <c:out value="Posted"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <c:out value="In Progress"/>
                                                </c:otherwise>
                                            </c:choose>
                                        </display:column>
                                    <display:column title="Action">
                                        <img id="report" onmouseover="tooltip.show('<strong>Preview</strong>',null,event);" onmouseout="tooltip.hide();"
                                           src="${path}/img/icons/search_over.gif" border="0"
                                         onClick="previewArInvoice('${invoicePoolList.id}')"/>
                                </display:column>
                                </display:table>
                        </div>
                    </td>
                </tr>
         </table>--%>
         
        <table class="tableBorderNew" width="100%">
            <tr class="tableHeadingNew">
                <td>Invoice Pool List</td>
            </tr>
            <tr>
                <td>
                    <div id="divtablesty1" style="border: thin; overflow: hidden;white-space: nowrap;">
                        <table width="100%" border="0" cellpadding="1" cellspacing="1" class="displaytagstyle" 
                            style="border: thin; overflow: scroll;">
                            <thead>
                                <tr>
                                 <th>Invoice Number</th><th>File No</th><th>Created Date</th><th>Posted Date</th><th>Dollar Amount</th><th>Bill To Party</th><th>User</th>
                                 <th>Description</th><th>Status</th><th>Action</th>
                                </tr>
                            </thead>
                            <% 
                                Integer id=0;
                                String invoiceNumber = "",fileNo = "", party = "",user = "", desc = "",status = "", bol = "",bookingNo = "",quuoteNo = "",
                                        moduleId = "", itemId = "",selectedMenu="";
                                String className = "odd";
                                Double amount = 0.0;
                                Date createdDate = new java.util.Date();
                                Date postedDate = new java.util.Date();
                                List<ArRedInvoice> arRedInvoiceList = new ArrayList<ArRedInvoice>();
                                Object obj = request.getAttribute("invoicePoolList");
                                arRedInvoiceList = (List<ArRedInvoice>)obj;
                                Iterator it = arRedInvoiceList.iterator();
                                while (it.hasNext()) {
                                    ArRedInvoice invoice = (ArRedInvoice)it.next();
                                    if (null != invoice.getId()) {
                                       id =  invoice.getId(); 
                                    }
                                    if (null != invoice.getInvoiceNumber()) {
                                       invoiceNumber = invoice.getInvoiceNumber(); 
                                    }
                                    if (null != invoice.getFileNo()) {
                                       fileNo = invoice.getFileNo(); 
                                    }
                                    if (null != invoice.getDate()) {
                                       createdDate = invoice.getDate(); 
                                    }
                                    if (null != invoice.getPostedDate()) {
                                       postedDate = invoice.getPostedDate(); 
                                    }
                                    if (null != invoice.getInvoiceAmount()) {
                                       amount = invoice.getInvoiceAmount(); 
                                    }
                                    if (null != invoice.getCustomerName()) {
                                       party = invoice.getCustomerName(); 
                                    }
                                    if (null != invoice.getInvoiceBy()) {
                                       user = invoice.getInvoiceBy(); 
                                    }
                                    if (null != invoice.getStatus()) {
                                       status = invoice.getStatus();
                                    }
                                    if (null != invoice.getNotes()) {
                                       desc = invoice.getNotes(); 
                                    }
                                    if (null != invoice.getBol()) {
                                       bol = invoice.getBol(); 
                                    }
                                    if (null != invoice.getBookingNo()) {
                                       bookingNo = invoice.getBookingNo(); 
                                    }
                                    if (null != invoice.getQuuoteNo()) {
                                       quuoteNo = invoice.getQuuoteNo(); 
                                    }
                                    if (null != invoice.getModuleId()) {
                                       moduleId = invoice.getModuleId(); 
                                    }
                                    if (null != invoice.getItemId()) {
                                       itemId = invoice.getItemId(); 
                                    }
                                    if (null != invoice.getSelectedMenu()) {
                                       selectedMenu = invoice.getSelectedMenu(); 
                                    }
                            %>
                            <tbody id="BlMicList">
                                <tr id="BlMiscPattern" class="<%=className%>" style="width:250px">
                                  <td><span id="tableInvoiceNumber"><%=invoiceNumber%></span></td>
                                  <td><span style="color:blue;cursor: pointer;text-decoration: underline" onclick="checkLockAndNavigatToBl('<%=fileNo%>','blId=<%=bol%>&bookingId=<%=bookingNo%>&quoteId=<%=quuoteNo%>&fileNumber=<%=fileNo%>','<%=moduleId%>','<%=itemId%>','<%=selectedMenu%>')">
                                     <%=fileNo%></span></td>
                                  <td><span id="tableCreatedDate">
                                          <fmt:formatDate var="createdDate" pattern="dd-MM-yyyy" value="<%=createdDate%>"/>
                                            ${createdDate}
                                      </span>
                                  </td>
                                   <td>
                                      <span id="tablePostedDate">
                                          <% if(status.equalsIgnoreCase("AR")){ %>
                                              <fmt:formatDate var="postDate" pattern="dd-MM-yyyy" value="<%=postedDate%>"/>
                                               ${postDate}
                                           <% } %>        
                                      </span>
                                   </td>
                                  <td><span id="tableAmount">
                                          <fmt:formatNumber  var="dollerAmount" pattern="###,###,##0.00" value="<%=amount%>"/>
                                            ${dollerAmount}
                                      </span>
                                  </td>
                                  <td><span id="tableParty"><%=party%></span></td>
                                  <td><span id="tableUser"><%=user%></span></td>
                                  <c:set var="description" value="<%=desc%>"/>
                                  <td class="pre-wrap width-400px">${string:blankSpaceWord(description,70,' ')}</td>
                                  <td><span id="tableStatus">
                                          <%
                                          if (status.equals("AR")) {
                                          %>
                                              <c:out value="Posted"/>
                                              <%
                                          } else {
                                              %>
                                              <c:out value="In Progress"/>
                                              <%
                                          }
                                          %>
                                          </span></td>
                                  <td><img id="report" onmouseover="tooltip.show('<strong>Preview</strong>',null,event);" onmouseout="tooltip.hide();"
                                           src="${path}/img/icons/search_over.gif" border="0"
                                         onClick="previewArInvoice(<%=id%>)"/></td>
                               </tr>
                            </tbody> 
                            <%
                                    if (className == "even") {
                                        className = "odd";
                                    } else {
                                        className = "even";
                                    }
                                }
                            %>
                        </table>
                    </div>
                </td>
            </tr>
        </table>
       </c:if>
        </html:form>
    </body>
    <script language="javascript">
        function checkLockAndNavigatToBl(fileNumber,path,moduleId,itemId,selectedMenu){
            var userId='${userId}';
            ProcessInfoBC.cheackFileINDB(fileNumber,userId,{callback:function(data){
                    if(data!=null && data!=""){
                        if(data == 'sameUser'){
                            alertNew("File "+fileNumber+ " is already opened in another window");
                            return;
                        }else{
                            alertNew(fileNumber+" This record is being used by "+data);
                        }
                    }else{
                        window.parent.changeChildsFromDisputedBl(path,fileNumber,moduleId,itemId,selectedMenu);
                    }
                    },async:false});
            }
            function validateDate(date){
                if(date.value!=""){
                    date.value = date.value.getValidDateTime("/","",false);
                    if(date.value==""||date.value.length>10){
                        alertNew("Please enter valid date");
                        document.getElementById(date.id).focus();
                    }
                }
            }
    </script>
</html>