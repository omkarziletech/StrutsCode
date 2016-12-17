
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="init.jsp" %>
<%@include file="/jsps/preloader.jsp" %>
<%@include file="colorBox.jsp" %>
<%@include file="../fragment/lclFormSerialize.jspf"  %>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/taglib.jsp" %>
<cong:javascript src="${path}/jsps/LCL/js/exportVoyageHblBatch.js"></cong:javascript>
    <html>
        <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>Export Voyage Batch</title>
        </head>
        <body>
            <div class="tableHeadingNew">House BL Batch</div>
        <cong:form action="/exportVoyageHblBatch.do" name="exportVoyageHblBatchForm" id="exportVoyageHblBatchForm">
            <cong:hidden id="methodName" name="methodName" /> 
            <cong:hidden id="headerId" name="headerId" value="${exportVoyageHblBatchForm.headerId}" />
            <cong:hidden id="voyageNumber" name="voyageNumber" value="${exportVoyageHblBatchForm.voyageNumber}" />            
            <cong:hidden id="pickedCargoBkg" name="pickedCargoBkg" value="${exportVoyageHblBatchForm.pickedCargoBkg}" />   
            <cong:hidden id="documentTypes" name="documentTypes"/>   
            <cong:hidden id="agentEmailAddress" name="agentEmailAddress"/>
            <table style="margin-left: 110px;margin-top: 10px;" border="0" cellspacing="10" class="textBoldforlcl">
                <tr> 
                    <td colspan="2" class="tableHeadingNew">Number of copies</td>
                    <td colspan="2" class="tableHeadingNew">Options for Bill of Lading</td>
                </tr>
                <tr><td colspan="4">&nbsp;</td></tr>
                <tr>
                    <td colspan="2" style="padding-bottom:1em;">Unit To Print
                        &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                        <html:select property="unitSSId" styleId="unitSSId" value="${exportVoyageHblBatchForm.unitSSId}"  
                                     styleClass="textlabelsBoldForTextBox verysmalldropdownStyleForText">
                            <html:option value="0">ALL</html:option>
                            <html:optionsCollection name="unitList"/>
                        </html:select>
                    </td>
                    <td style="padding-bottom:1em;">DR# To Print
                        <cong:autocompletor name="fileNumber"  id="fileNumber"  template="one" paramFields="headerId" 
                                            position="right" scrollHeight="100px" query="HBL_DR" shouldMatch="true" fields="fileNumberId" />
                        <cong:hidden id="fileNumberId" name="fileNumberId" />
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <table border="0">
                            <tr>
                                <td>
                                    <cong:text name="unsignedOriginal"  id="unsignedOriginal" styleClass="unsignedCount" maxlength="3"
                                               style="width:45px;" onkeyup="checkNumberAndDecimal(this)" container="null"
                                               value="${exportVoyageHblBatchForm.unsignedOriginal}"/>
                                </td>
                                <td class="unsigned">Original (Unsigned)</td>
                                <td></td>
                                <td>
                                    <cong:radio id="rated" name="billOfLading" value="RATED" container="null" onclick="setSelectOption();"/>
                                    Rated
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text name="original" id="original" styleClass="originalCount" maxlength="3"
                                               style="width:45px;" onkeyup="checkNumberAndDecimal(this)" container="null"
                                               value="${exportVoyageHblBatchForm.original}"/>
                                </td>
                                <td class="original">Original (signed)</td>
                                <td></td>
                                <td>
                                    <cong:radio id="unRated"  name="billOfLading" value="UNRATED" container="null" onclick="setSelectOption();"/>
                                    Unrated
                                </td>   
                            </tr>
                            <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text name="nonNegotiable"  id="nonNegotiable" styleClass="nonNegotiableCount" maxlength="3"
                                               style="width:45px;" onkeyup="checkNumberAndDecimal(this)" container="null"
                                               value="${exportVoyageHblBatchForm.nonNegotiable}"/>
                                </td>
                                <td class="nonNeg" colspan="2">Non-Negotiable (Unsigned)
                                    <span title="Email"><cong:checkbox name="negotiableEmail" id="negotiableEmail" 
                                                   container="null" onclick="selectRadioOption();"/></span>
                                </td>
                                <td>
                                    <cong:radio id="combineBunker" name="billOfLading" value="BUNKER" container="null" onclick="setSelectOption();"/>
                                    Combine Accesorials into Bunker
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text name="signedNonNegotiable"  id="signedNonNegotiable" styleClass="signedCount" maxlength="3"
                                               style="width:45px;" onkeyup="checkNumberAndDecimal(this)" container="null"
                                               value="${exportVoyageHblBatchForm.signedNonNegotiable}"/>
                                </td>
                                <td class="signed">Non-Negotiable (Signed)</td>
                                <td></td>
                                <td>
                                    <cong:radio id="oceanFreight"  name="billOfLading" value="OCNBLPC" container="null" onclick="setSelectOption();"/>
                                    Ocean Freight & BL/PC Only
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3">                      
                                </td>
                                <td>
                                    <cong:radio id="consolidateCharges" name="billOfLading" value="OCNFRT" container="null" onclick="setSelectOption();"/>
                                    Consolidate all charges to Ocean Freight
                                </td>
                            </tr>
                            <tr>
                                <td colspan="3"></td>
                                <td>
                                    <cong:radio id="collectCharges" name="billOfLading" value="COLLECT" container="null" onclick="setSelectOption();"/>
                                    Collect Charges Only
                                </td>
                            </tr>
                            <tr><td colspan="4"></td></tr>
                            <tr>
                                <td colspan="3"></td>
                                <td>
                                    <cong:radio id="prepaidCharges" name="billOfLading" value="PREPAID" container="null" onclick="setSelectOption();"/>
                                    Prepaid Charges Only
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
                <tr>
                    <td colspan="4">
                        <table>
                            <tr>
                                <td  style="padding-top:1.5em;">
                                    <cong:text name="frieghtInvoice"  id="frieghtInvoice" styleClass="frieghtCount" maxlength="3"
                                               style="width:45px;" onkeyup="checkNumberAndDecimal(this);setFreight(this,'frieghtColInvoice');" container="null"
                                               value="${exportVoyageHblBatchForm.frieghtInvoice}"/>
                                </td>
                                <td class="frieght" style="padding-top:1.5em;">Freight Invoice
                                    <span title="Email"> 
                                        <cong:checkbox name="frieghtEmail" id="frieghtEmail" container="null" 
                                                       onclick="setFreigtCheckBox(this,'frieghtEmailCollect')"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text id="frieghtInvoiceCollect" name="frieghtInvoiceCollect"  container="null" 
                                               styleClass="frieghtColCount" maxlength="3"  style="width:45px;" 
                                               onkeyup="checkNumberAndDecimal(this);setFreight(this,'frieghtInvoice');" 
                                               value="${exportVoyageHblBatchForm.frieghtInvoiceCollect}" />
                                </td>
                                <td class="frieghtCol">Freight Invoices Collect Only 
                                    <span title="Email"> 
                                        <cong:checkbox id="frieghtEmailCollect" name="frieghtEmailCollect" container="null" 
                                                       onclick="setFreigtCheckBox(this,'frieghtEmail')"/>
                                    </span>
                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text id="unitManifest" name="unitManifest" container="null"
                                               styleClass="manifestCount" maxlength="3" style="width:45px;"
                                               onkeyup="checkNumberAndDecimal(this);"
                                               value="${exportVoyageHblBatchForm.unitManifest}" />
                                </td>
                                <td class="manifestCol"> Manifest
                                    <span title="Email">
                                        <cong:checkbox id="unitManifestEmail" name="unitManifestEmail" container="null"
                                                       onclick="" />
                                    </span>

                                </td>
                            </tr>
                               <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text id="unitLargePrintManifest" name="unitLargePrintManifest" container="null"
                                               styleClass="unitLargePrintManifestCount" maxlength="3" style="width:45px;"
                                               onkeyup="checkNumberAndDecimal(this);"
                                               value="${exportVoyageHblBatchForm.unitLargePrintManifest}" />
                                </td>
                                <td class="LargePrintManifestCol"> Large Print Manifest
                                    <span title="Email">
                                        <cong:checkbox id="unitLargePrintManifestEmail" name="unitLargePrintManifestEmail" container="null"
                                                       onclick="" />
                                    </span>

                                </td>
                            </tr>
                               <tr>
                                <td style="padding-top:.5em;">
                                    <cong:text id="unitMiniConsolidationManifest" name="unitMiniConsolidationManifest" container="null"
                                               styleClass="unitMiniConsolidationManifestCount" maxlength="3" style="width:45px;"
                                               onkeyup="checkNumberAndDecimal(this);"
                                               value="${exportVoyageHblBatchForm.unitMiniConsolidationManifest}" />
                                </td>
                                <td class="MiniConsolidationManifestCol"> Mini Consolidation Manifest
                                    <span title="Email">
                                        <cong:checkbox id="unitMiniConsolidationManifestEmail" name="unitMiniConsolidationManifestEmail" container="null"
                                                       onclick="" />
                                    </span>

                                </td>
                            </tr>
                            <tr>
                                <td style="padding-top:.5em">
                                    <cong:text id="unitUnratedDockReceipt" name="unitUnratedDockReceipt" container="null"
                                               styleClass="unitUnratedDockReceiptCount" maxlength="3" style="width:45px;"
                                               onkeyup="checkNumberAndDecimal(this);" 
                                               value="${exportVoyageHblBatchForm.unitUnratedDockReceipt}"/>
                                </td>
                                <td class="UnratedDockReceiptCol">Unrated Dock Receipt
                                    <span title="Email">
                                        <cong:checkbox id="unitUnratedDockReceiptEmail" name="unitUnratedDockReceiptEmail" container="null"
                                                       onclick="" />
                                    </span>
                                </td>
                            </tr>
                        </table>
                    </td> 
                </tr>
            </table>
            <table style="margin-left:130px;margin-top:10px;" border="0" class="textBoldforlcl">
                <tr>
                    <td width="40%"></td>
                    <td>
                        <input type="button" value=" Print " onclick="printAction()" class="button-style1" id="print"/>
                        <input type="button" value=" Email " onclick="emailAction()" class="button-style1" id="email"/>
                    </td>
                    <td>
                        <input type="button" name="email" id="emailMe" value="Email Me" container="null" 
                               class="button-style1" onclick="submitEmailMe(this)"/>
                    </td>
                    <td>
                        <input type="button" value=" Status " onclick="openSchedulerPopUp();" 
                               class="${isEmailorFax ? 'green-background':'button-style1'}" id="status"/>
                    </td>
                </tr>
            </table>
            <table style="margin-left:150px;margin-top:10px; width: 60%"
                   class="tableBorderNew">
                <tr class="tableHeadingNew">
                    <td>Printer Details</td>
                </tr>
                <tr>
                    <td>
                        <div id="result-header" class="table-banner green">
                            <div class="float-left">
                                <input type="hidden" id="printer" value="${not empty printerList ? true : false}"/>
                                <c:choose>
                                    <c:when test="${fn:length(printerList)>1}">
                                        ${fn:length(printerList)} Printers found.
                                    </c:when>
                                    <c:otherwise>
                                        <span class="${not empty printerList ? '' :'redBold'}">
                                            ${not empty printerList ? '1' : 'No'} Printer Found</span>
                                        </c:otherwise>
                                    </c:choose>
                            </div>
                        </div>
                        <c:if test="${not empty printerList}">
                            <table class="dataTable" border="0">
                                <thead>
                                    <tr>
                                        <th>Document Name</th>
                                        <th>Printer Name</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    <c:forEach var="print" items="${printerList}" varStatus="printCount">
                                        <c:if test="${printCount.count==1}">
                                            <cong:hidden id="printerName" name="printerName" 
                                                         styleClass="printerName" value="${print.value}"/>
                                        </c:if>
                                        <c:set var="rowStyle" value="${rowStyle eq 'oddStyle' ? 'evenStyle' : 'oddStyle'}"/>
                                        <tr class="${rowStyle}">
                                            <td class="documentName">${print.key}</td>
                                            <td class="printerName">${print.value}</td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                        </c:if>
                    </td>
                </tr>
            </table>
        </cong:form>
    </body>
    <div id="email-container" class="static-popup" style="display: none;width: 600px;height: 300px;">
        <table width="100%" class="tableBorderNew"  border="0">
            <tr class="tableHeadingNew">
                <td colspan="2">
                    Compose Mail
                </td>
            </tr>
            <tr><td><br/></td></tr>
            <tr>
                <td class="textlabelsBoldforlcl">TO:</td>
                <td>
                    <cong:text name="toEmailAddress"  id="toEmailAddress"  style="width:300px;"
                               container="null"  value="${exportVoyageHblBatchForm.toEmailAddress}"/>
                    <input type="checkbox" name="agentContact" id="agentContact" onclick="setAgentContact(this)" title="Select Agent"/>
            </tr>
            <tr><td><br/></td></tr>
            <tr>
                <td class="textlabelsBoldforlcl">CC:</td>
                <td>
                    <cong:text name="ccEmailAddress"  id="ccEmailAddress"  style="width:300px;"
                               container="null"  value="${exportVoyageHblBatchForm.ccEmailAddress}"/>
                </td>
            </tr>
            <tr><td><br/></td></tr>
            <tr>
                <td class="textlabelsBoldforlcl">BCC:</td>
                <td>
                    <cong:text name="bccEmailAddress"  id="bccEmailAddress"  style="width:300px;"
                               container="null"  value="${exportVoyageHblBatchForm.bccEmailAddress}"/>
                </td>
            </tr>
            <tr><td><br/></td></tr>
            <tr>
                <td class="textlabelsBoldforlcl">Subject:</td>
                <td>
                    <cong:text name="emailSubject"  id="emailSubject"  style="width:300px;"
                               container="null"  value="${exportVoyageHblBatchForm.emailSubject}"/>
                </td>
            </tr>
            <tr><td><br/></td></tr>
            <tr>
                <td class="textlabelsBoldforlcl">Message:</td>
                <td>
                    <textarea name="emailMessage"  id="emailMessage"  rows="5" cols="35"
                              container="null"  value="${exportVoyageHblBatchForm.emailMessage}"></textarea>
                </td>
            </tr>
            <tr><td><br/></td></tr>
            <tr>
                <td></td>
                <td  align="center">
                    <input type="button"  value="Submit" id="saveEmail" align="center" class="button" onclick="submitEmail();"/>
                    <input type="button"  value="Cancel" id="cancelEmailId" align="center" class="button" onclick="cancelEmail();"/>
                </td>
            </tr>
        </table>
    </div>
</html>
