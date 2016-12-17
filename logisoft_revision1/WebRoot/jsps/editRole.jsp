<%--
    Document   : editRole
    Created on : Jul 13, 2010, 4:02:12 PM
    Author     : Vinay
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@include file="includes/jspVariables.jsp" %>
<%@include file="includes/baseResources.jsp" %>
<%@include file="includes/resources.jsp" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <script language="javascript" src="${path}/js/common.js"></script>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Add Permission</title>
        <link href="${path}/css/layout/second-tabs.css" type="text/css" rel="stylesheet"/>       
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-ext.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.centreIt-1.1.5.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery-impromptu.js"></script>
        <link type="text/css" rel="stylesheet" href="${path}/css/jquery/jquery.tooltip.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.tooltip.js"></script>
        <script src="${path}/js/tab/tab.js" type="text/javascript" ></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $("[title != '']").not("link").tooltip();
                $("ul.htabs").tabs("> .pane", {effect: 'fade', current: 'selected', initialIndex: 0, onClick: function () {
                        var index = $("ul.htabs li.selected").find("a").attr("tabindex");
                        var src = $("#src" + index).val();
                        if ($("#tab" + index).attr("src") === '') {
                            $("#tab" + index).attr("src", src);
                        }
                        $("#tab" + index).height($(document).height() - 45);
                    }
                });
            });

        </script>
    </head>
    <div id="ConfirmYesOrNo" class="alert">
        <p class="alertHeader" style="width: 100%;padding-left: 3px;"><b>Confirmation</b></p>
        <p id="innerText2" class="containerForAlert" style="width: 100%;padding-left: 3px;"></p>
        <form style="text-align:right;padding-right:4px;padding-bottom: 4px;">
            <input type="button"  class="buttonStyleForAlert" value="Yes"
                   onclick="confirmYes()">
            <input type="button"  class="buttonStyleForAlert" value="No"
                   onclick="confirmNo()">
        </form>
    </div>
    <body class="whitebackgrnd" topmargin="1">
        <html:form action="/editRoleDuties" name="editRoleDForm" type="com.gp.cong.logisoft.struts.form.EditRoleDForm" method="post" scope="request">
            <html:hidden property="action" value="update"/>
            <html:hidden property="roleId" value="${currRole.roleId}"/>
            <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%" >
                <tr class="tableHeadingNew">
                    <td colspan="4">
                        &nbsp;<c:out value="${currRole.roleName}" />
                    </td>
                </tr>              
            </table>
            <br>
            <ul class="htabs">
                <li><a href="javascript: void(0)" tabindex="0">COMMON</a></li>
                <li><a href="javascript: void(0)" tabindex="1">ACCOUNTING</a></li>
                <li><a href="javascript: void(0)" tabindex="2">TRADING PARTNER</a></li>
                <li><a href="javascript: void(0)" tabindex="3">FCL</a></li>
                <li><a href="javascript: void(0)" tabindex="4">LCL IMPORTS</a></li>
                <li><a href="javascript: void(0)" tabindex="5">LCL EXPORTS</a></li>                                  
            </ul>

            <div class="pane"> 
                <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%">
                    <tbody>
                        <tr>
                            <td style="height: 10px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="deleteAttachedDocuments" name="currRole">
                                    <span class="textLabelsBold">Delete Attached Documents</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[75]}"/>
                            </td>
                            <td>
                                <html:checkbox  property="deleteManualNotes" name="currRole">
                                    <span class="textLabelsBold">Delete Manual Notes</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[80]}" />
                            </td>
                            <td>
                                <html:checkbox  property="changeLogoPreference" name="currRole">
                                    <span class="textLabelsBold">Change Logo Preference</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[83]}" />
                            </td>
                            <td>
                                <html:checkbox property="showFollowUpTasks" styleId="showFollowUpTasks" name="currRole">
                                    <span class="textLabelsBold">Show Followup Tasks</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[43]}" />
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="accessCorrectionPrintFax" name="currRole">
                                    <span class="textLabelsBold">Access Correction Print/Fax</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[18]}" />
                            </td>                           
                            <td>
                                <html:checkbox property="byPassVoyage" styleId="byPassVoyage" name="currRole">
                                    <span class="textLabelsBold">Force Voyage on LCL Exp Booking</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[47]}" />
                            </td>
                            <td>
                                <html:checkbox property="terminateWithoutInvoice" styleId="terminateWithoutInvoice" name="currRole">
                                    <span class="textLabelsBold">Terminate Booking Without Invoice</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[48]}" />
                            </td>
                            <td></td>
                        </tr>   
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="reversePostedInvoices" styleId="reversePostedInvoices" name="currRole">
                                    <span class="textLabelsBold">Reverse Posted Invoices</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[51]}"/>
                            </td>
                            <td>
                                <html:checkbox property="deleteLclCommodity" styleId="deleteLclCommodity" name="currRole">
                                    <span class="textLabelsBold">Delete Lcl Commodity</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[42]}" />
                            </td>
                            <td>
                                <html:checkbox property="unmanifestLclUnit" styleId="unmanifestLclUnit" name="currRole">
                                    <span class="textLabelsBold">Unit Unmanifest</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[57]}" />
                            </td>
                            <td>
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="pane"> 
                <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%">
                    <tbody>
                        <tr>
                            <td style="height: 10px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="arInquiryChangeCustomer" name="currRole">
                                    <span class="textLabelsBold">AR Inquiry - Change Customer</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[20]}"/>
                            </td> 
                            <td>
                                <html:checkbox property="accrualsCreateNew" name="currRole">
                                    <span class="textLabelsBold">Accruals - Create new Accruals</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[24]}" />
                            </td>
                            <td>
                                <html:checkbox property="apSpec" name="currRole">
                                    <span class="textLabelsBold">AP Specialist</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[1]}"/>
                            </td>
                            <td>
                                <html:checkbox property="arBatchReversal" styleId="arBatchReversal" name="currRole">
                                    <span class="textLabelsBold">AR Batch - Reversal of Cash batches</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[49]}" />
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="inactivateAccruals" styleId="inactivateAccruals" name="currRole">
                                    <span class="textLabelsBold">Inactivate Accruals</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[53]}"/>
                            </td>
                            <td>
                                <html:checkbox property="arInquiryMakeAdjustments" name="currRole">
                                    <span class="textLabelsBold">AR Inquiry - Make Adjustments</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[21]}"/>
                            </td>
                            <td>
                                <html:checkbox property="apPayment" styleId="apPayment" name="currRole">
                                    <span class="textLabelsBold">Show All AP Payment</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[50]}"/>
                            </td>
                            <td>
                                <html:checkbox property="bankAccountCreateNew" name="currRole">
                                    <span class="textLabelsBold">Bank Account - Add/Edit Bank account</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[25]}"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="arBatchShowallUsersBatch" name="currRole">
                                    <span class="textLabelsBold">AR Batch - Manage All Users Batches</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[22]}"/>
                            </td>
                            <td>
                                <html:checkbox property="arInqMngr" name="currRole">
                                    <span class="textLabelsBold">AR Inquiry Manager</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[2]}"/>
                            </td>
                            <td>
                                <html:checkbox property="viewAccountingScanAttach" name="currRole">
                                    <span class="textLabelsBold">View Accounting Scan/Attach</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[35]}"/>
                            </td>
                            <td>
                                <html:checkbox property="batchAccMngr" name="currRole">
                                    <span class="textLabelsBold">Batch Account Manager</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[3]}"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="supervisor" name="currRole">
                                    <span class="textLabelsBold">Supervisor</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[5]}"/>
                            </td>
                            <td>
                                <html:checkbox property="arBatchDirectGlAccount" name="currRole">
                                    <span class="textLabelsBold">AR Batch - Enter Direct GL Account</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[23]}"/>
                            </td>
                            <td>
                                <html:checkbox property="checkRegisterController" styleId="checkRegisterController" name="currRole">
                                    <span class="textLabelsBold">Check Register Controller</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[40]}"/>
                            </td>
                            <td>
                                <html:checkbox property="journalEntryClosedPeriod" name="currRole">
                                    <span class="textLabelsBold">Journal Entry - Select closed period to post</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt="" title="${tooltips[26]}"/>
                            </td>
                        </tr>                        
                    </tbody>
                </table>
            </div>  
            <div class="pane"> 
                <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%">
                    <tbody>
                        <tr>
                            <td style="height: 10px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="changeTpType" name="currRole">
                                    <span class="textLabelsBold">Change TP Type</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[16]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="tpShowAddress" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show Address</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[28]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="tpShowContactConfig" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show Contact Config</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[32]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="displayDefaults" styleId="displayDefaults" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Display Defaults</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[44]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="tpShowGeneralInfo" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show General Info</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[29]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="tpShowConsigneeInfo" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show Consignee Config</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[33]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="arConfigTabReadOnly" name="currRole">
                                    <span class="textLabelsBold">AR Config Tab Read Only</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[77]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td> 
                            <td>
                                <html:checkbox  property="changeSalesCode" name="currRole">
                                    <span class="textLabelsBold">Change Sales Code</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[81]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="tpShowArConfig" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show Ar Config</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[30]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="disableOrEnableTp" name="currRole">
                                    <span class="textLabelsBold">Disable/Enable TradingPartner</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[11]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="tpSetVendorType" name="currRole">
                                    <span class="textLabelsBold">Add Trading Partner - Set Vendor as account type</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[27]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="tpShowApConfig" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show Ap Config</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[31]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="tpShowCtsConfig" name="currRole">
                                    <span class="textLabelsBold">Trading Partner - Show CTS Config</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[63]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="creditHolder" name="currRole">
                                    <span class="textLabelsBold">Credit Hold - Show Hold check boxes</span>
                                </html:checkbox>
                                <span class="hotspot" 
                                      onmouseover="tooltip.show('${tooltips[4]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox  property="allowImportCfsVendor" name="currRole">
                                    <span class="textLabelsBold">Allow Import CFS Vendor</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[79]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="ecuDesignation" name="currRole">
                                    <span class="textLabelsBold">ECU Designation</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[64]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="editEciAcct" name="currRole">
                                    <span class="textLabelsBold">Edit ECI Account Number</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[14]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="vendorOtherthanFF" name="currRole">
                                    <span class="textLabelsBold">Create Vendor Other than FF</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[13]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="changeMaster" name="currRole">
                                    <span class="textLabelsBold">Change Master</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[17]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="creditHoldOpsUser" styleId="creditHoldOpsUser" name="currRole">
                                    <span class="textLabelsBold">Credit Hold - OPS Level User</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[52]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td> <html:checkbox property="manageECIAccountLink" styleId="manageECIAccountLink" name="currRole">
                                    <span class="textLabelsBold">Manage ECI Account Link </span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[94]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span></td>   
                        </tr>
                    </tbody>
                </table>
            </div>     
            <div class="pane"> 
                <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%">
                    <tbody>
                        <tr>
                            <td style="height: 10px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="editDeferralCharge" name="currRole">
                                    <span class="textLabelsBold">Edit Deferral Charge</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[76]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td> 
                            <td>
                                <html:checkbox property="deleteImportContainers" name="currRole">
                                    <span class="textLabelsBold">Delete Import FCL Containers</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[66]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="accessVoidButton" name="currRole">
                                    <span class="textLabelsBold">Access Void Button</span>
                                </html:checkbox>
                                <span class="hotspot" 
                                      onmouseover="tooltip.show('${tooltips[0]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="unmanifest" name="currRole">
                                    <span class="textLabelsBold">Unmanifest</span>
                                </html:checkbox>
                                <span class="hotspot" 
                                      onmouseover="tooltip.show('${tooltips[6]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>

                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="postCorrections" name="currRole">
                                    <span class="textLabelsBold">Post Corrections</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[9]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="revCorrections" name="currRole">
                                    <span class="textLabelsBold">Reverse Post CN</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[10]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="resendAes" name="currRole">
                                    <span class="textLabelsBold">Resend AES</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[34]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="resendAccruals" name="currRole">
                                    <span class="textLabelsBold">Resend Accruals</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[39]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="audit" name="currRole">
                                    <span class="textLabelsBold">Audit Bl</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[37]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="reopenBl" name="currRole">
                                    <span class="textLabelsBold">Reopen Bl</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[7]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="closeBl" name="currRole" styleId="close_Bl">
                                    <span class="textLabelsBold">Close Bl</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[36]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="cancelAudit" name="currRole" >
                                    <span class="textLabelsBold">Cancel Audit</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[38]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="allowRoutedAgent" styleId="allowRoutedAgent" name="currRole">
                                    <span class="textLabelsBold">Routed Agent</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[41]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="takeOwnershipOfDisputedBL" name="currRole">
                                    <span class="textLabelsBold">Take ownership of Disputed BLs</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[19]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="sslPrepaidCollect" styleId="sslPrepaidCollect" name="currRole">
                                    <span class="textLabelsBold">SSL BL PPD/COLL</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[46]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="deleteCostandCharges" name="currRole">
                                    <span class="textLabelsBold">Delete Cost/Charges</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[70]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="allowtoEnterSpotRate" name="currRole">
                                    <span class="textLabelsBold">Allow to Enter Spot Rate</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[73]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="addPredefinedRemarks" name="currRole">
                                    <span class="textLabelsBold">Add Pre-defined Remarks for Imports</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[15]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="auditOverride" styleId="auditOverride" name="currRole">
                                    <span class="textLabelsBold">Imports Audit Override</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[45]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="showDetailedCharges" name="currRole">
                                    <span class="textLabelsBold">Show Detailed Charges</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[8]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="accessDisputedBlNotesAndAck" name="currRole">
                                    <span class="textLabelsBold">Access Disputed BL Notes</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[12]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="disabledContainerwithAPcosts" name="currRole">
                                    <span class="textLabelsBold">Disable Container with AP costs</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[84]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="no997EdiSubmission" name="currRole">
                                    <span class="textLabelsBold">No 997 EDI Submission</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[96]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <div class="pane"> 
                <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%">
                    <tbody>
                        <tr>
                            <td style="height: 10px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox  property="linkDrAfterDispositionPort" name="currRole">
                                    <span class="textLabelsBold">Link DR after Disposition Port</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[82]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="lclImportVoyageReopen" name="currRole">
                                    <span class="textLabelsBold">LCL Import Voyage Reopen</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[78]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="importsVoyagePod" name="currRole">
                                    <span class="textLabelsBold">Verify imports Voyage POD</span>
                                </html:checkbox>
                                <span class="hotspot" 
                                      onmouseover="tooltip.show('${tooltips[74]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="lclUnitOSD" name="currRole">
                                    <span class="textLabelsBold">LCL OSD</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[71]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="deleteImportsUnit" name="currRole">
                                    <span class="textLabelsBold">Delete LCL Imports Unit</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[67]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="lclImportVoyageClose" name="currRole">
                                    <span class="textLabelsBold">LCL Import Voyage Close</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[68]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="lclEcuInvoiceMapp" name="currRole">
                                    <span class="textLabelsBold">Eculine Invoice Mapping</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[72]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td> 
                            <td>
                                <html:checkbox property="bookingTerminate" styleId="bookingTerminate" name="currRole">
                                    <span class="textLabelsBold">LCL Import Terminate</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[61]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="lclImportVoyageAudit" name="currRole">
                                    <span class="textLabelsBold">LCL Import Voyage Audit</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[69]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="deleteDisposition" name="currRole">
                                    <span class="textLabelsBold">Delete Disposition</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[65]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="lclVoyageOwner" styleId="lclVoyageOwner" name="currRole">
                                    <span class="textLabelsBold">LCL Voyage Owner</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[62]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                            <td>
                                <html:checkbox property="deleteVoyage" styleId="deleteVoyage" name="currRole">
                                    <span class="textLabelsBold">Delete LCL Voyage</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[55]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>   
                        <tr>
                        <td>
                                <html:checkbox property="lclImportAllowTransshipment" name="currRole">
                                    <span class="textLabelsBold">Allow T/S to Yes after PORT</span>
                                </html:checkbox>
                                <span class="hotspot"
                                      onmouseover="tooltip.show('${tooltips[98]}', '60', event);"
                                      onmouseout="tooltip.hide()" >
                                    <img src="${path}/img/icons/help-icon.gif" />
                                </span>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 20px" colspan="4"></td>
                        </tr>                                     
                    </tbody>
                </table>
            </div>
            <div class="pane"> 
                <table border="0" cellpadding="0" cellspacing="0" class="tableBorderNew" style="width: 100%">
                    <tbody>
                        <tr>
                            <td style="height: 10px" colspan="6"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="unPost" styleId="unPost" name="currRole">
                                    <span class="textLabelsBold">Unpost BL</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Unpost BL"/>
                            </td>
                            <td>
                                <html:checkbox property="changeVoyage" styleId="changeVoyage" name="currRole">
                                    <span class="textLabelsBold">Allowed to change Voyage in LCL</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Allowed to change Voyage in LCL" />
                            </td>
                            <td>
                                <html:checkbox property="openLclUnit" styleId="openLclUnit" name="currRole">
                                    <span class="textLabelsBold">Open LCL Unit</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Allow User To Open LCL Unit"/>
                            </td>
                            <td>
                                <html:checkbox property="lclCurrentLocation" styleId="lclCurrentLocation" name="currRole">
                                    <span class="textLabelsBold">Update Lcl Current Location</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Update Lcl Current Location"/>
                            </td>
                            <td> <html:checkbox property="deleteNotes" styleId="deleteNotes" name="currRole">
                                    <span class="textLabelsBold">Delete Notes </span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Delete Lcl Exports Notes"/>
                            </td>
                            <td> <html:checkbox property="addTemplates" styleId="addTemplates" name="currRole">
                                    <span class="textLabelsBold">Add/Edit Templates</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Add/Edit Template"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 15px" colspan="6"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="showUncompleteUnits" styleId="showUncompleteUnits" name="currRole">
                                    <span class="textLabelsBold">Show Incomplete Units</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Show Incomplete Units" />
                            </td>
                            <td><html:checkbox property="editLclBlOwner" styleId="editLclBlOwner" name="currRole">
                                    <span class="textLabelsBold">Change BL Owner</span></html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="Change BL Owner"  />
                            </td>
                            <td>
                                <html:checkbox property="lclExpVoyageOwner" styleId="lclExpVoyageOwner" name="currRole">
                                    <span class="textLabelsBold">Change Voyage Owner</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" title="${tooltips[62]}"/>
                            </td>
                            <td>
                                <html:checkbox property="expDeleteVoyage" styleId="expDeleteVoyage" name="currRole">
                                    <span class="textLabelsBold">Delete LCL Voyage</span>
                                </html:checkbox>
                                <img alt="" src="${path}/img/icons/help-icon.gif"  title="Allow User To Delete LCL Voyage"/>
                            </td>
                            <td>
                                <html:checkbox property="reverseCob" styleId="reverseCob" name="currRole">
                                    <span class="textLabelsBold">Reverse COB</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="${tooltips[87]}"/>
                            </td>
                            <td> <html:checkbox property="voidLCLBLafterCOB" styleId="voidLCLBLafterCOB" name="currRole">
                                    <span class="textLabelsBold">Void LCL BL after COB </span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt=""  title="${tooltips[88]}"/>
                            </td>
                        </tr>

                        <tr>
                            <td style="height: 15px" colspan="6"></td>
                        </tr>

                        <tr>
                            <td>
                                <html:checkbox property="aesRequiredForReleasingDRs" styleId="aesRequiredForReleasingDRs" name="currRole">
                                    <span class="textLabelsBold">AES required for Releasing DRs</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt=""  title="${tooltips[90]}"/>
                            </td>
                            <td>
                                <html:checkbox property="aesRequiredForPostingBLs" styleId="aesRequiredForPostingBLs" name="currRole">
                                    <span class="textLabelsBold">AES required for Posting BLs</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif"  alt=""  title="${tooltips[91]}"/>
                            </td>
                            <td>
                                <html:checkbox property="batchHsCode" styleId="batchHsCode" name="currRole">
                                    <span class="textLabelsBold">Enable Batch HS Code</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="${tooltips[95]}"/>
                            </td>
                            <td> <html:checkbox property="warehouseQuickBkg" styleId="warehouseQuickBkg" name="currRole">
                                    <span class="textLabelsBold">Warehouse Quick Booking</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Warehouse Quick Booking"/>
                            </td>
                            <td> <html:checkbox property="bkgVoyageReleaseDr" styleId="bkgVoyageReleaseDr" name="currRole">
                                    <span class="textLabelsBold">Booked for Voyage Required for Releasing DRs</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" 
                                     title="Booked for Voyage Required for Releasing DRs"/>
                            </td>
                            <td>
                                <html:checkbox property="lclBookingContact" styleId="lclBookingContact" name="currRole">
                                    <span class="textLabelsBold">Require Booking Contact on LCL Exports Booking</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt="" 
                                     title="Require Booking Contact on LCL Exports Booking"/>
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 15px" colspan="6"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="voyageCloseAuditUndo" styleId="voyageCloseAuditUndo" name="currRole">
                                    <span class="textLabelsBold">LCL Export Voyage Close/Audit Undo </span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="${tooltips[89]}" />
                            </td>
                            <td>
                                <html:checkbox property="lclManifestPostedBl" styleId="lclManifestPostedBl" name="currRole">
                                    <span class="textLabelsBold">Allow Manifest without a Posted BL</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Allow Manifest without a Posted BL" />
                            </td>
                            <td>
                                <html:checkbox property="weightChangeAfterRelease" styleId="weightChangeAfterRelease" name="currRole">
                                    <span class="textLabelsBold">Allow changing pcs/cube/weight after release</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Allow changing actual pcs/cube/weight after release" />
                            </td>
                            <td>
                                <html:checkbox property="preventExpRelease" styleId="preventExpRelease" name="currRole">
                                    <span class="textLabelsBold">Prevent Express Release if PPD and No Credit</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Prevent Express Release if PPD and No Credit" />
                            </td>
                            <td>
                                <html:checkbox property="deleteUnits" styleId="deleteUnits" name="currRole">
                                    <span class="textLabelsBold">Delete unit</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Delete unit" />
                            </td>
                            <td>
                                <html:checkbox property="removeDrHold" styleId="removeDrHold" name="currRole">
                                    <span class="textLabelsBold">Remove DR Hold</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Remove DR Hold" />
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 15px" colspan="6"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="bypassRelayCheck" styleId="bypassRelayCheck" name="currRole">
                                    <span class="textLabelsBold">Bypass Relay Check</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Bypass Relay check when opening Voyages" />
                            </td>   
                            <td>
                                <html:checkbox property="defaultLoadAllReleased" styleId="defaultLoadAllReleased" name="currRole">
                                    <span class="textLabelsBold">Default Load Screen to All Released</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Default Load Screen to All Released" />
                            </td>
                            <td>
                                <html:checkbox property="lclQuoteClient" styleId="lclQuoteClient" name="currRole">
                                    <span class="textLabelsBold">Force Client on LCL Exports quotes</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Force Client on LCL Exports quotes" />
                            </td>
                            <td>
                                <html:checkbox property="pickDrWarnings" styleId="pickDrWarnings" name="currRole">
                                    <span class="textLabelsBold">Show Pick DR Warnings</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Show Pick DR Warnings" />
                            </td>
                            <td>
                                <html:checkbox property="changeBLCommodityAfterCOB" styleId="changeBLCommodityAfterCOB" name="currRole">
                                    <span class="textLabelsBold">Change BL commodity line after COB</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Change BL commodity line after COB" />
                            </td>
                            <td>
                                <html:checkbox property="changeDefaultFF" styleId="changeDefaultFF" name="currRole">
                                    <span class="textLabelsBold"> Default FF to NOFFAA0001 </span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Default FF to NOFFAA0001 in LCL Exports Quote/Booking" />
                            </td>
                        </tr>
                        <tr>
                            <td style="height: 15px" colspan="6"></td>
                        </tr>
                        <tr>
                            <td>
                                <html:checkbox property="allowChangeDisposition" styleId="allowChangeDisposition" name="currRole">
                                    <span class="textLabelsBold"> Allow Changes to Disposition </span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Allow Changes to Disposition" />
                            </td>
                       
                            <td>
                                <html:checkbox property="batchHotCode" styleId="batchHotCode" name="currRole">
                                    <span class="textLabelsBold"> Enable Batch HOT Code</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Enable Batch HOT Code for Consolidate" />
                            </td>
                            <td>
                                <html:checkbox property="lclBookingDefaultERT" styleId="lclBookingDefaultERT" name="currRole">
                                    <span class="textLabelsBold">Default Bookings to ERT No</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Default Bookings to ERT No" />                                            
                            </td>
                            <td>
                                <html:checkbox property="defaultNoeeiLowVal" styleId="defaultNoeeiLowVal" name="currRole">
                                    <span class="textLabelsBold">Default Booking to AES-NOEEI-LV</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Default Booking to AES-NOEEI-LV" />
                            </td>
                            <td>
                                <html:checkbox property="defaultDocsRcvd" styleId="defaultDocsRcvd" name="currRole">
                                    <span class="textLabelsBold">Default Docs Rcvd to Yes</span>
                                </html:checkbox>
                                <img src="${path}/img/icons/help-icon.gif" alt=""  title="Default Docs Rcvd to Yes" />
                            </td>
                            <td></td>
                        </tr>
                        <tr>
                            <td style="height: 15px" colspan="6"></td>
                        </tr>
                    </tbody>
                </table>
            </div>

            <table border="0" cellpadding="0" cellspacing="0" class="" style="width: 100%">
                <tr>
                <tr>
                    <td style="height: 10px" colspan="4"></td>
                </tr>
                <td colspan="8" align="center">
                    <input type="submit" value="Submit" class="buttonStyleNew"/>
                    <input type="button" value="Cancel" class="buttonStyleNew" onclick="cancelAction()"/>
                </td>
            </tr>
        </table>   
        <script type="text/javascript">
            function cancelAction() {
                document.editRoleDForm.action.value = "";
                document.editRoleDForm.submit();
            }
            var roleToClear = "";
            function resetForm(roleId) {
                roleToClear = roleId;
                confirmYesOrNo("Please Note that all Property Elements will be cleared. Proceed Y/N", "clearProperty");
            }
            function confirmMessageFunction(id1, id2) {
                if (id1 == 'clearProperty' && id2 == 'NO') {
                    yesFunction();
                } else if (id1 == 'clearProperty' && id2 == 'yes') {
                    document.editRoleDForm.action.value = id1;
                    document.editRoleDForm.submit();
                }
            }
            </script>
        </html:form>
</body>
</html>
