<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld" prefix="display"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ include file="../includes/jspVariables.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <base href="${basePath}">
        <title>My JSP 'SedFiling.jsp' starting page</title>
        <%@include file="../includes/resources.jsp" %>
        <%@include file="../includes/baseResources.jsp" %>
    </head>
    <body class="whitebackgrnd">
        <div id="cover" style="width: expression(document.body.offsetWidth+'px');height: expression(document.body.offsetHeight+120+'px');display: none"></div>
       <html:form action="/aesHistory" type="com.gp.cong.logisoft.struts.form.AesHistoryForm" scope="request">
            <table align="center" width="100%" cellpadding="0" cellspacing="0">
                <tr>
                    <td>
                        <table align="center" width="100%" cellpadding="0" cellspacing="0" class="tableBorderNew">
                            <tr class="tableHeadingNew">Aes Tracking</tr>
                            <tr><td><table>
                                        <tr class="textlabelsBold">
                                            <td>ITN Number&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:text property="itnNumber" styleClass="textlabelsBoldForTextBox"  size="25" ></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                            <td>File Number&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:text property="fileNumber" styleClass="textlabelsBoldForTextBox"  size="12" ></html:text>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
                                            <td>Aes Status&nbsp;&nbsp;&nbsp;</td>
                                            <td><html:select property="status" style="width:150px;" styleClass="BackgrndColorForTextBox">
                                                    <html:option value="">Select Status</html:option>
                                                    <html:option value="Shipment Added">Shipment Added</html:option>
                                                    <html:option value="Shipment Changed">Shipment Changed</html:option>
                                                    <html:option value="Shipment Rejected">Shipment Rejected</html:option>
                                                </html:select>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                            </td>
                                            <td><input type="button" class="buttonStyleNew" value="Search" onclick="searchAES()"></td>
                                            </tr>
                                        </table></td></tr>
                            </table>
                        </td>
                    </tr>
                    <tr>
                        <td>
                            <table cellspacing="0" cellpadding="0"  width="100%" border="0" class="tableBorderNew" >
                                <tr class="tableHeadingNew">
                                    <td>AES Tracking List<td>
                                </tr>
                                <tr>
                                    <td>
                                    <c:if test="${!empty aesHistoryList}">
                                        <c:set var="i" value="0"></c:set>
                                        <display:table name="${aesHistoryList}" class="displaytagstyleNew" pagesize="15"
                                                       id="aesHistory" sort="list" requestURI="/aesHistory.do?">
                                            <display:setProperty name="paging.banner.some_items_found">
                                                <span class="pagebanner">
                                                    <font color="blue">{0}</font>Aes Detail Displayed,For more Data click on Page Numbers.
                                                    <br>
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.one_items_found">
                                                <span class="pagebanner">
							     			One {0} displayed. Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.all_items_found">
                                                <span class="pagebanner">
							     			{0} {1} Displayed, Page Number
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="basic.msg.empty_list">
                                                <span class="pagebanner">
										    No Records Found.
                                                </span>
                                            </display:setProperty>
                                            <display:setProperty name="paging.banner.placement" value="bottom" />
                                            <display:setProperty name="paging.banner.item_name" value="Filename"/>
                                            <display:setProperty name="paging.banner.items_name" value="Filenames"/>
                                            <display:column  property="fileNumber" sortable="true" title="Shipment Number"></display:column>
                                            <display:column  property="itn" sortable="true" title="ITN"></display:column>
                                            <display:column property="status" title="STATUS" sortable="true"></display:column>
                                            <display:column  property="aesException" sortable="true" title="AES Exception"></display:column>
                                            <display:column  property="processedDate" sortable="true" title="Processed Date"></display:column>
                                           <%-- <display:column title="Action" >
                                                    <img alt=""src="<c:url value="/img/icons/pubserv.gif"/>" border="0" style="cursor: pointer;"
                                                         onclick="window.location.href='${path}/sedFiling.do?buttonValue=goToSedFiling&mode=edit&fileNo=${aesHistory.fileNumber}';"/>
                                            </display:column>--%>
                                            <c:set var="i" value="${i+1}"></c:set>
                                        </display:table>
                                    </c:if>
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>
            </table>
            <html:hidden property="buttonValue" />
        </html:form>
    </body>
    <script type="text/javascript">
        function searchAES(){
            document.aesHistoryForm.buttonValue.value = "search";
            document.aesHistoryForm.submit();
        }
    </script>
</html>
