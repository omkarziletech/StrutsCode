<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@include file="/taglib.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<link href="css/layout.css" type="text/css" rel="stylesheet" />
<link href="css/lable-fields.css" type="text/css" rel="stylesheet" />
    <body>
    <cong:div style="width:100%; float:left;" id="mainDiv">
        <cong:table id="mainTable" width="97%" border="0"
                    cellpadding="0" cellspacing="0" styleClass="tableBorderNew">

            <cong:hidden name="fileNumber" value="fileNumber"/>
            <cong:hidden name="moduleId" id="moduleId" value="${lclPrintForm.moduleId}"/>
            <cong:tr>
                <cong:td>
                    <cong:table width="100%" cellpadding="0" cellspacing="0">
                        <cong:tr styleClass="tableHeadingNew">
                            <cong:td>
                                Document List for File No:<span style="color: red" >${lclPrintForm.fileNumber}</span>
                            </cong:td>
                            <cong:td width="25%"></cong:td>
                            <cong:td align="right">
                                <input type="button" class="button-style1" value="Email Cover Page" id="emailFormId"/>
                                <input type="button" class="button-style1" value="Fax Cover Page" id="faxFormId"/>
                                <input type="button" class="button-style1" id="status" value="Status" style="width:70px" onclick="openSchedulerPopUp()"/>
                                <input type="button" id="contactButton"  class="button-style1" value="Contacts" onclick="showContactsPopup()"/>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td width="99%" colspan="3" align="center">
                                <c:set var="count" value="1"/>
                                <display:table name="${lclPrintList}" defaultsort="1" class="displaytagstyleNew" id="printList" style="width:80%" sort="external">
                                    <display:setProperty name="paging.banner.some_items_found">
                                        <span class="pagebanner"> <font color="blue">{0}</font>
                                            Print Details Displayed,For more Scan Details click on Page
                                            Numbers. <br> </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.one_item_found">
                                        <span class="pagebanner"> One {0} displayed. Page
                                            Number </span>
                                        </display:setProperty>
                                        <display:setProperty name="paging.banner.all_items_found">
                                        <span class="pagebanner"> {0} {1} Displayed, Page
                                            Number </span>
                                        </display:setProperty>
                                        <display:setProperty name="basic.msg.empty_list">
                                        <span class="pagebanner"> No Records Found. </span>
                                    </display:setProperty>
                                    <display:setProperty name="paging.banner.placement"
                                                         value="bottom"/>
                                    <display:setProperty name="paging.banner.item_name"
                                                         value="document"/>
                                    <display:setProperty name="paging.banner.items_name"
                                                         value="documents"/>
                                    <display:column title=""  >
                                        <c:out value="${printList.documentName}"></c:out>
                                    </display:column>
                                    <display:column title="<br/>Print" sortable="true" headerClass="sortable">

                                    </display:column>

                                    <display:column title="<br/>Email" sortable="true" headerClass="sortable"/>

                                    <display:column title="<br/>Fax" sortable="true" headerClass="sortable"/>


                                    <display:column title="<br/>Email Me" sortable="true" headerClass="sortable"/>

                                    <display:column title="<br/>Email SP" sortable="true" headerClass="sortable"/>

                                    <display:column title="<br/>Preview">
                                        <img src="${path}/jsps/LCL/images/search_over.gif" alt="preview" width="20" height="20"
                                             onmouseover="tooltip.show('<strong>Preview</strong>');"onmouseout="tooltip.hide();"
                                             onclick="previewReport('${lclPrintForm.fileNumberId}','${lclPrintForm.fileNumber}','${printList.screenName}','${printList.documentName}','${count}');"/>
                                    </display:column>


                                    <c:set var="count" value="${count+1}"/>
                                </display:table>

                            </cong:td>
                        </cong:tr>
                    </cong:table>

                </cong:td>
            </cong:tr>

        </cong:table>
        <cong:td>&nbsp;</cong:td>
        <cong:td>
            <input type="button" value="Submit" class="button-style1"/>
            <input type="button" value="Cancel" class="button-style1" onclick="closePopup()"/>
        </cong:td>
    </cong:div>
    <cong:div id="emailTemplate" style="display: none">
        <cong:table width="100%" styleClass="tableBorderNew" style="border: 10px;"
                    border="0">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td colspan="2">
                    Compose Mail
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBold" width="10%">
                    TO
                </cong:td>
                <cong:td>
                    <input type="text" name="toAddress" id="toAddress"
                           size="50" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBold">
                    CC
                </cong:td>
                <cong:td>
                    <input type="text" name="ccAddress" size="50" id="ccAddress" />
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBold">
                    BCC
                </cong:td>
                <cong:td>
                    <input type="text" name="bccAddress" size="50" id="bccAddress"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBold">
                    Subject
                </cong:td>
                <cong:td>
                    <input type="text" size="50" name="emailSubject" id="emailSubject"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td styleClass="textlabelsBold" valign="top">
                    Message
                </cong:td>
                <cong:td>
                    <cong:textarea name="mailMessage" id="mailMessage" cols="50" rows="10"/>
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td colspan="2" align="center">
                    <input type="button" class="button-style1" value="OK"/>
                    <input type="button" value="Cancel" class="cancelBut button-style1"/>
                </cong:td>
            </cong:tr>
        </cong:table>
    </cong:div>
    <cong:div id="faxTemplate" style="display: none;width: 100%;">
        <cong:table style="align:center" id="mainTable" width="100%" border="0"
                    cellpadding="0" cellspacing="0" styleClass="tableBorderNew">
            <cong:tr styleClass="tableHeadingNew">
                <cong:td colspan="2">
                    Fax
                </cong:td>
            </cong:tr>
            <cong:tr>
                <cong:td>
                    <cong:table style="align:center" id="subTable" width="70%" border="0"
                                cellpadding="0" cellspacing="1">
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                To
                            </cong:td>
                            <cong:td >
                                <cong:text name="toName" id="toName"
                                           styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                Fax Number
                            </cong:td>
                            <cong:td>
                                <cong:text name="toFaxNumber" id="toFaxNumber" styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                From
                            </cong:td>
                            <cong:td>
                                <cong:text name="fromName" id="fromName"
                                           styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                Fax Number
                            </cong:td>
                            <cong:td>
                                <cong:text name="fromFaxNumber" id="fromFaxNumber" styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                Business Phone
                            </cong:td>
                            <cong:td>
                                <cong:text name="businessPhone" id="businessPhone" styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                Home Phone
                            </cong:td>
                            <cong:td>
                                <cong:text name="homePhone" id="homePhone" styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                Subject
                            </cong:td>
                            <cong:td>
                                <cong:text name="subject" id="subject" styleClass="textlabelsBoldForTextBox"></cong:text>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td styleClass="textlabelsBold">
                                Message
                            </cong:td>
                            <cong:td>
                                <cong:textarea name="message" cols="50" rows="7" id="message" styleClass="textlabelsBoldForTextBox"></cong:textarea>
                            </cong:td>
                        </cong:tr>
                        <cong:tr>
                            <cong:td colspan="2">
                                <cong:table width="100%" cellpadding="0" cellspacing="0">
                                    <cong:tr>
                                        <cong:td align="center">
                                            <input type="button" class="button-style1" value="OK" onClick="setFaxFormValues()"/>
                                            <input type="button" value="Cancel" onclick="cancelFax()"
                                                   class="cancelBut button-style1"/>
                                        </cong:td>
                                    </cong:tr>
                                </cong:table>
                            </cong:td>
                        </cong:tr>
                    </cong:table>
                </cong:td>
            </cong:tr>

        </cong:table>
    </cong:div>
    <script>
        $('#emailFormId').click(function() {
            $("#emailTemplate").slideDown(1000);
            $("#mainDiv").slideUp(1000);
        });
        $('#faxFormId').click(function() {
            $("#faxTemplate").slideDown(1000);
            $("#mainDiv").slideUp(1000);
        });
        $('.cancelBut').click(function() {
            $("#emailTemplate").slideUp('slow');
            $("#mainDiv").slideDown('slow');
            $("#faxTemplate").slideUp('slow');
        });
        function closePopup()
        {
            parent.$.fn.colorbox.close();
        }
        function previewReport(fileId,fileNumber,screenName,documentName) {
            jQuery.ajaxx({//printReport method changed due to change in FileName
                data: {
                    className: "com.gp.cong.lcl.dwr.LclPrintUtil",
                    methodName: "lclPrintReport",
                    param1: fileId,
                    param2: fileNumber,
                    param3: screenName,
                    param4: screenName,
                    request: "true"
                },
                success: function(data){
                viewFile(data);
                }
            });
        }
        function viewFile(file) {
            var win = window.open('${path}/servlet/PdfServlet?fileName='+file,'_new','width=1000,height=650,toolbar=no,directories=no,status=no,linemenubar=no,scrollbars=no,resizable=no,modal=yes');
            window.onblur = function() {
                win.focus();
            }
        }


    </script>
</body>