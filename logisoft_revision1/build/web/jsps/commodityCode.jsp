<%@include file="../jsps/LCL/init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@include file="/jsps/includes/resources.jsp" %>
<%@include file="/jsps/includes/jspVariables.jsp" %>
<%@include file="/taglib.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<html>
    <body>
        <html:form action="/commodityCode.do" name="commodityCodeForm" type="com.gp.cvst.logisoft.struts.form.lcl.CommodityCodeForm" scope="request">
            <br/>
            <table width="99.5%" align="center" border="0" cellpadding="0" cellspacing="0" class="tableBorderNew">
                <tr class="tableHeadingNew"><td>Commodity Search</td></tr>
                <tr><td>
                        <table width="100%">
                            <tr>
                                <td class="textlabelsBoldforlcl">Code</td>
                                <td>
                                    <cong:autocompletor id="code" name="code" template="two" query="COMMODITY_TYPE_CODE" container="NULL" value="${code}"
                                                        styleClass="textlabelsBoldForTextBox" shouldMatch="true" fields="desc" width="400" scrollHeight="200px" />
                                </td>
                                <td class="textlabelsBoldforlcl">Description</td>
                                <td>
                                    <cong:autocompletor id="desc" name="desc" template="two" query="COMMODITY_TYPE_NAME" container="NULL" value="${description}"
                                                        styleClass="textlabelsBoldForTextBox" shouldMatch="true" fields="code" width="400"  scrollHeight="200px" />
                                </td>
                                <td align="left">
                                    <input type="button" class="button-style1"  value='Search' onclick="commSearch('${path}')"/>
                                </td>
                            </tr>
                            <tr><td colspan="5"></td></tr>
                        </table>
                        <display:table name="${commodityList}" id="commList" class="dataTable"  pagesize="25" requestURI="/commodityCode.do" style="width:100%">
                            <display:setProperty name="paging.banner.placement" value="bottom"/>
                            <display:column  title="CODE" style="width:50Px;" sortable="true">
                                <a style="cursor: pointer;" onclick="editCommodity('${path}','${commList.id}','${commList.code}','${commList.active}','${fn:replace(commList.descEn,'\'','\\&#39;')}','${commList.hazmat}','${commList.highVolumeDiscount}','${commList.refrigerationRequired}','${commList.defaultErt}','${commList.remarks}')">
                                    <u>
                                        <font color="blue">
                                            <div align="center">${commList.code}</div>
                                        </font>
                                    </u>
                                </a>
                            </display:column>
                            <display:column title="DESCRIPTION" style="width:300Px;">
                                <div style="width:300px;">${commList.descEn}</div>
                            </display:column>
                            <display:column title="ACTIVE" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${commList.active==true}">
                                        <c:set var="active" value="YES"/>
                                        <div align="center">${active}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="active" value="NO"/>
                                        <div align="center">${active}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="HAZMAT" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${commList.hazmat==true}">
                                        <c:set var="hazmt" value="YES"/>
                                        <div align="center">${hazmt}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="hazmt" value="NO"/>
                                        <div align="center">${hazmt}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="DEFAULT ERT" style="width:50Px;">
                                <c:choose>
                                    <c:when test="${commList.defaultErt==true}">
                                        <c:set var="defErt" value="YES"/>
                                        <div align="center">${defErt}</div>
                                    </c:when>
                                    <c:otherwise>
                                        <c:set var="defErt" value="NO"/>
                                        <div align="center">${defErt}</div>
                                    </c:otherwise>
                                </c:choose>
                            </display:column>
                            <display:column title="REMARKS" style="width:300Px;">
                                <div>${commList.remarks}</div>
                            </display:column>
                        </display:table>
                    </td></tr></table>
                </html:form>
    </body>
    <script type="text/javascript" >
        function editCommodity(path,id,code,active,descrip,hazmt,highVolume,refrigerationReq,defaultErt,remarks){
            var href=path+"/commodityCode.do?methodName=editCommodity&id="+id+"&code="+code+"&active="+active+"&descEn="+descrip+ "&hazmat="+hazmt+
                "&highVolumeDiscount="+highVolume+"&refrigerationRequired="+refrigerationReq+"&defaultErt="+defaultErt+"&remarks="+remarks;
            document.location.href=href;
        }
        function commSearch(path){
            var code=$('#code').val();
            var desc=$('#desc').val();
            var href=path+"/commodityCode.do?methodName=search&code="+code+"&descEn="+desc;
            document.location.href=href;
        }
    </script>
</html>