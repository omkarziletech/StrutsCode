<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/resources.jsp" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@include file="../includes/baseResources.jsp" %>
<%@include file="../includes/resources.jsp" %>
<jsp:useBean id="lclSearchTemplate" scope="request" type="com.gp.cong.logisoft.domain.lcl.LclSearchTemplate"/>
<html>
    <head>
    <c:set var="path" value="${pageContext.request.contextPath}"/>
    <script language="javascript" src="${path}/js/common.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title></title>
</head>
<body>
    <html:form action="/searchTemplate" name="searchTemplateForm" type="com.gp.cong.logisoft.struts.form.SearchTemplateForm" scope="request">
        <table width="100%" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
            <tr class="tableHeadingNew"><td>LCL Default Template</td>
                <td align="right"></td>
            </tr>
            <tr><td>
                    <table width="100%">
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="qu" styleId="qu" name="lclSearchTemplate" >
                                    <span class="textLabelsBold">QU</span>
                                </html:checkbox>
                                <html:hidden property="methodName" styleId="methodName" name="searchTemplateForm"/>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="bk" styleId="bk" name="lclSearchTemplate">
                                    <span class="textLabelsBold">BK</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="bl" styleId="bl" name="lclSearchTemplate">
                                    <span class="textLabelsBold">BL</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="hz" styleId="hz" name="lclSearchTemplate">
                                    <span class="textLabelsBold">HZ</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="edi" styleId="edi" name="lclSearchTemplate">
                                    <span class="textLabelsBold">EDI</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="fileNo" styleId="fileNo" name="lclSearchTemplate">
                                    <span class="textLabelsBold">FILE NO</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="tr" styleId="tr" name="lclSearchTemplate">
                                    <span class="textLabelsBold">TR</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="status" styleId="status" name="lclSearchTemplate">
                                    <span class="textLabelsBold">STATUS</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="doc" styleId="doc" name="lclSearchTemplate">
                                    <span class="textLabelsBold">DOC</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="dateReceived" styleId="dateReceived" name="lclSearchTemplate">
                                    <span class="textLabelsBold">DATE RECEIVED</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="pcs" styleId="pcs" name="lclSearchTemplate">
                                    <span class="textLabelsBold">PCS</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="cube" styleId="cube" name="lclSearchTemplate">
                                    <span class="textLabelsBold">CUBE</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="weight" styleId="weight" name="lclSearchTemplate">
                                    <span class="textLabelsBold">WEIGHT</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="origin" styleId="origin" name="lclSearchTemplate">
                                    <span class="textLabelsBold">ORIGIN</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="pol" styleId="pol" name="lclSearchTemplate">
                                    <span class="textLabelsBold">POL</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="pod" styleId="pod" name="lclSearchTemplate">
                                    <span class="textLabelsBold">POD</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="destination" styleId="destination" name="lclSearchTemplate">
                                    <span class="textLabelsBold">DESTINATION</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="shipper" styleId="shipper" name="lclSearchTemplate">
                                    <span class="textLabelsBold">SHIPPER</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="fwd" styleId="fwd" name="lclSearchTemplate">
                                    <span class="textLabelsBold">FORWADER</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="consignee" styleId="consignee" name="lclSearchTemplate">
                                    <span class="textLabelsBold">CONSIGNEE</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="billTm" styleId="billTm" name="lclSearchTemplate">
                                    <span class="textLabelsBold">BILL TM</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="aesBy" styleId="aesBy" name="lclSearchTemplate">
                                    <span class="textLabelsBold">AES BY</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="quoteBy" styleId="quoteBy" name="lclSearchTemplate">
                                    <span class="textLabelsBold">QUOTE BY</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="bookedBy" styleId="bookedBy" name="lclSearchTemplate">
                                    <span class="textLabelsBold">BOOKED BY</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="cons" styleId="cons" name="lclSearchTemplate">
                                    <span class="textLabelsBold">CONS</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="bookedSaildate" styleId="bookedSaildate" name="lclSearchTemplate">
                                    <span class="textLabelsBold">BOOKED SAIL DATE</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="hotCodes" styleId="hotCodes" name="lclSearchTemplate">
                                    <span class="textLabelsBold">HOT CODES</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="loadLrd" styleId="loadLrd" name="lclSearchTemplate">
                                    <span class="textLabelsBold">LOAD LRD</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold"><html:checkbox property="relayOverride" styleId="relayOverride" name="lclSearchTemplate">
                                    <span class="textLabelsBold">RELAY OVERRIDE</span>
                                </html:checkbox>
                            </td>
                            <td class="textLabelsBold"><html:checkbox property="originLrd" styleId="originLrd" name="lclSearchTemplate">
                                    <span class="textLabelsBold">ORIGIN LRD</span>
                                </html:checkbox></td>
                            <td class="textLabelsBold"><html:checkbox property="currentLocation" styleId="currentLocation" name="lclSearchTemplate">
                                    <span class="textLabelsBold">Current Location</span>
                                </html:checkbox></td>
                             <td class="textLabelsBold"><html:checkbox property="pn" styleId="pn" name="lclSearchTemplate">
                                    <span class="textLabelsBold">PN</span>
                                </html:checkbox>
                            </td>
                        </tr>
                        <tr>
                            <td class="textLabelsBold">
                                <input type="button" value="Save" class="buttonStyleNew"  onclick="submitTemplate('saveTemplate')"/>
                            </td>
                            <td class="textLabelsBold"></td>
                            <td class="textLabelsBold"></td>
                            <td class="textLabelsBold"></td>

                        </tr>
                    </table>
                </td></tr>
        </table>
    </html:form>

</body>
<script type="text/javascript">
    function submitTemplate(methodName) {
        document.getElementById('methodName').value = methodName;
        document.searchTemplateForm.submit();
    }
</script>
</html>
