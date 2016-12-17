<%-- 
    Document   : EmailScheduler
    Created on : Jan 27, 2014, 11:46:01 PM
    Author     : Meiyazhakan
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Search Scheduler</title>
        <%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
        <%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
        <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
        <%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
        <%@ taglib prefix="cong" tagdir="/WEB-INF/tags/cong"%>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <%@include file="/jsps/preloader.jsp" %>
        <%@include file="../includes/resources.jsp" %>
        <link type="text/css" rel="stylesheet"  href="${path}/css/common.css" />
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.datepick.css"/>
        <link type="text/css" rel="stylesheet" href="${path}/js/lightbox/lightbox.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.new.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.date.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.datepick.js"></script>
        <script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.js'></script>
        <script type='text/javascript' src='${path}/jsps/LCL/js/autocomplete/localdata.js'></script>
        <link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/main.css" />
        <link rel="stylesheet" type="text/css" href="${path}/jsps/LCL/js/autocomplete/jquery.autocomplete.css" />
        <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
        <script type="text/javascript" src="${path}/jsps/admin/js/EmailScheduler.js"></script>
    </head>
    <body>
        <html:form  action="/emailSchedulers.do" styleId="emailSchedulerForm" name="emailSchedulerForm" type="com.gp.cong.logisoft.struts.form.EmailSchedulerForm" scope="request">
            <input type="hidden" id="emailCheck" name="emailCheck"/>
            <input type="hidden" id="emailId" name="emailId"/>
            <input type="hidden" id="path" name="path" value="${path}"/>
            <input type="hidden" id="isPrintPopUp" name="isPrintPopUp" value="${emailSchedulerForm.isPrintPopUp}"/>
            <table width="100%" style="border:1px solid #dcdcdc">
                <thead>
                    <tr id="tableBanner">
                        <td colspan="5">Search Scheduler</td>
                        <td align="right"><input type="button" value="New Mail" class="button" onclick="openNewMailWindow('${path}');"/></td>
                    </tr>
                    <tr>
                        <td id="labelText">Start Date</td>
                        <td>
                            <html:text property="startDate" styleId="startDate" styleClass="textbox" maxlength="10"/>
                        </td>
                        <td id="labelText">End Date</td>
                        <td>
                            <html:text property="endDate" styleId="endDate" styleClass="textbox" maxlength="10"/>
                        </td>
                        <td id="labelText">To Email/Fax</td>
                        <td>
                            <input name="toEmailOrFax" class="textbox" id="toEmailOrFax"  value="${emailSchedulerForm.toEmailOrFax}"/>
                        </td>
                    </tr>
                    <tr>
                        <td id="labelText">Status</td>
                        <td>
                            <html:select property="status" styleId="status" value="${emailSchedulerForm.status}" style="width: 130px;" styleClass="dropdown">
                                <html:option value="Completed">Completed</html:option>
                                <html:option value="Pending">Pending</html:option>
                                <html:option value="Failed">Failed</html:option>
                            </html:select>
                        </td>
                        <td id="labelText">Document Id</td>
                        <td>
                            <input id="fileName" name="fileName" class="textbox" size="18" value="${emailSchedulerForm.fileName}"/>
                        </td>
                        <td id="labelText">User Name</td>
                        <td>
                            <cong:autocompletor name="userName" id="userName" width="400" scrollHeight="200px" container="NULL" position="left"
                                                styleClass="textbox" query="SALES_PERSON" fields="loginId" template="one" value="${emailSchedulerForm.userName}"/>
                        </td>
                    </tr>
                    <tr class="textlabelsBold">
                        
                       <td align="right" id="labelText" colspan="3"  style="padding-bottom:10px;">Name</td>
                        <td>
                            <html:select property="filterByName" styleId="filterByName" styleClass="dropdown" style="width:134px">
                                <html:option value="">Select</html:option>
                                <html:option value="ACCRUALS_INVOICE">ACCRUALS_INVOICE</html:option>
                                <html:option value="ACHFTPUPLOAD">ACHFTPUPLOAD</html:option>
                                <html:option value="agolwala">agolwala</html:option>
                                <html:option value="ajagiell">ajagiell</html:option>
                                <html:option value="aperalta">aperalta</html:option>
                                <html:option value="APPAYMENT">APPAYMENT</html:option>
                                <html:option value="AR Invoice">AR Invoice</html:option>
                                <html:option value="ARBatch">ARBatch</html:option>
                                <html:option value="ARCreditHold">ARCreditHold</html:option>
                                <html:option value="ArStatement">ArStatement</html:option>
                                 <html:option value="AR_INVOICE">AR_INVOICE</html:option>
                                <html:option value="aseagrav">aseagrav</html:option>
                                <html:option value="Barrel D/R">Barrel D/R</html:option>
                                <html:option value="BL">BL</html:option>
                                <html:option value="Booking">Booking</html:option>
                                <html:option value="Booking EDI">Booking EDI</html:option>
                                <html:option value="COB Reminder">COB Reminder</html:option>
                                <html:option value="Corrections">Corrections</html:option>
                                <html:option value="CreditDebitNote">CreditDebitNote</html:option>
                                <html:option value="dhuang">dhuang</html:option>
                                <html:option value="Disposition Change">Disposition Change</html:option>
                                <html:option value="EditLCLBLPool">EditLCLBLPool</html:option>
                                <html:option value="FCL BL MOVE">FCL BL MOVE</html:option>
                                <html:option value="FCLBLMOVE">FCLBLMOVE</html:option>
                                <html:option value="FclBuy">FclBuy</html:option>
                                <html:option value="FollowUpNotes">FollowUpNotes</html:option>
                                <html:option value="frosales">frosales</html:option>
                                <html:option value="gcrespo">gcrespo</html:option>
                                <html:option value="gluzbet">gluzbet</html:option>
                                <html:option value="ilopez">ilopez</html:option>
                                <html:option value="Invoice">Invoice</html:option>
                                <html:option value="iruiz">iruiz</html:option>
                                <html:option value="Japan AFR Log">Japan AFR Log</html:option>
                                <html:option value="jcastril">jcastril</html:option>
                                <html:option value="jestrell">jestrell</html:option>
                                <html:option value="jjuarez">jjuarez</html:option>
                                <html:option value="kbalcaza">kbalcaza</html:option>
                                <html:option value="KNBooking">KNBooking</html:option>
                                <html:option value="KNSailing">KNSailing</html:option>
                                <html:option value="lbedoya">lbedoya</html:option>
                                <html:option value="LCL Export House BL">LCL Export House BL</html:option>
                                <html:option value="LCLBL">LCLBL</html:option>
                                <html:option value="LCLBL Changes">LCLBL Changes</html:option>
                                <html:option value="LCLBL COB">LCLBL COB</html:option>
                                <html:option value="LCLBL Manifest">LCLBL Manifest</html:option>
                                <html:option value="LCLBL Posting">LCLBL Posting</html:option>
                                <html:option value="LclBooking">LclBooking</html:option>
                                <html:option value="LclCreditDebitNote">LclCreditDebitNote</html:option>
                                <html:option value="LCLDeliveryOrder">LCLDeliveryOrder</html:option>
                                <html:option value="LCLIMPBooking">LCLIMPBooking</html:option>
                                <html:option value="LclImpUnitReports">LclImpUnitReports</html:option>
                                <html:option value="LCLImpUnits">LCLImpUnits</html:option>
                                <html:option value="LCLQuotation">LCLQuotation</html:option>
                                <html:option value="LCLSSMaster">LCLSSMaster</html:option>
                                <html:option value="LCLUnits">LCLUnits</html:option>
                                <html:option value="lcolon">lcolon</html:option>
                                <html:option value="lhenriquez">lhenriquez</html:option>
                                <html:option value="lkallichar">lkallichar</html:option>
                                <html:option value="mgayle">mgayle</html:option>
                                <html:option value="mmesa">mmesa</html:option>
                                <html:option value="MultiQuote">MultiQuote</html:option>
                                <html:option value="nkocher">nkocher</html:option>
                                <html:option value="nsimon">nsimon</html:option>
                                <html:option value="PaperworkReminder">PaperworkReminder</html:option>
                                <html:option value="Quotation">Quotation</html:option>
                                <html:option value="rlowe">rlowe</html:option>
                                <html:option value="rsanchez">rsanchez</html:option>
                                <html:option value="SALES_REPORT">SALES_REPORT</html:option>
                                <html:option value="TERMINAL_REPORT">TERMINAL_REPORT</html:option>
                                <html:option value="volivero">volivero</html:option>
                                <html:option value="VOYAGE INVOICE">VOYAGE INVOICE</html:option>
                                <html:option value="Voyage Notification">Voyage Notification</html:option>
                                
                           </html:select>
                        </td>
                       
                        <td id="labelText">Limit</td>
                        <td>
                            <html:select property="limit" styleId="limit" styleClass="dropdown" style="width:134px">
                                <html:option value="250">250</html:option>
                                <html:option value="500">500</html:option>
                                <html:option value="1000">1000</html:option>
                                <html:option value="5000">5000</html:option>
                            </html:select>
                        </td>
                    </tr>
                    <tr>
                        <td align="right" colspan="3"  style="padding-bottom:10px;">
                            <input type="button" class="button" onclick="searchMail();" value="Search" name="search"/>
                            <input type="button" class="button" onclick="clearAllValues();" value="Reset" name="reset"/>
                        </td>
                        <td></td>
                        <td></td>
                  
                    </tr>
                </thead>
            </table>
            <br/>
            <table width="100%" style="border:1px solid #dcdcdc">
                <thead>
                    <tr id="tableBanner">
                        <td colspan="9">Search Results</td>
                        <td align="right">
                            <c:if test="${not empty emailSchedulerList}">
                                <input type="button" value="Send" class="button" onclick="sendMail();" id="sendMails"/>
                            </c:if>
                        </td>
                    </tr>
                </thead>
                <tr><td colspan="10" id="emails">
                        <c:if test="${not empty emailSchedulerList}">
                            <div id="result-header" class="table-banner green">
                                <div class="float-left">
                                    <c:choose>
                                        <c:when test="${fn:length(emailSchedulerList)>1}">
                                            ${fn:length(emailSchedulerList)} files found.
                                        </c:when>
                                        <c:otherwise>1 file found.</c:otherwise>
                                    </c:choose>
                                </div>
                            </div>
                            <table class="display-table" id="emailList">
                                <thead>
                                    <tr>
                                        <th></th>
                                        <th>Name</th>
                                        <th>File Location</th>
                                        <th>To Email/Fax</th>
                                        <th>Type</th>
                                        <th>Status</th>
                                        <th>NoOfTries</th>
                                        <th>EmailDate</th>
                                        <th>DocumentId</th>
                                        <th>UserName</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="email" items="${emailSchedulerList}">
                                        <c:choose>
                                            <c:when test="${rowStyle eq 'odd'}">
                                                <c:set var="rowStyle" value="even"/>
                                            </c:when>
                                            <c:otherwise>
                                                <c:set var="rowStyle" value="odd"/>
                                            </c:otherwise>
                                        </c:choose>
                                        <tr class="${rowStyle}">
                                            <td><input type="checkbox" id="emailCheck" name="emailCheck" class="emailChecks" value="${email.id}"/></td>
                                            <td>${email.name}</td>
                                            <td>
                                                <span title="${email.fileLocation}">
                                                    ${fn:substring(email.fileLocation,0,50)}
                                            </td>
                                            <td><span title="To:${fn:replace(email.toAddress,',','<br/>')}<br/> Cc:${fn:replace(email.ccAddress,',','<br/>')}<br/>Bcc:${fn:replace(email.bccAddress,',','<br/>')}">${fn:substring(email.toAddress,0,50)}
                                                </span>
                                            
                                            </td>
                                            <td>${email.type}</td>
                                            <td>${email.status}</td>
                                            <td>${email.noOfTries}</td>
                                            <td>
                                                <fmt:formatDate pattern="dd-MMM-yyyy hh:mm a" var="emailDate" value="${email.emailDate}"/>
                                                ${emailDate}
                                            </td>
                                            <td>${email.moduleId}</td>
                                            <td>${fn:toUpperCase(email.userName)}</td>
                                            <td>
                                                <img src="${path}/img/icons/preview.gif" onclick="previewPdf('${path}', '${email.fileLocation}');" title="View Report"/>
                                                <img src="${path}/img/icons/view.gif" onclick="viewEmailDetails('${path}', '${email.id}');" title="View Cover Page"/>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                    </td></tr>
            </table>
            <html:hidden property="methodName" styleId="methodName"/>
        </html:form>
    </body>
</html>
