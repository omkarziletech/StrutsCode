<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@include file="init.jsp" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@include file="/taglib.jsp" %>
<% pageContext.setAttribute("singleQuotes", "'"); %>
<cong:javascript src="${path}/jsps/LCL/js/currencyConverter.js"/>
<html>
    <body>
        <cong:form name="lclQuoteCommodityForm" id="lclQuoteCommodityForm" action="/lclQuoteCommodity.do">
           <%-- <cong:hidden name="orgId" id="orgId" value=""/>
            <cong:hidden name="destId" id="destId" value=""/> --%>
            <cong:table width="100%">
                <cong:tr>
                    <div align="left" id="pager" class="pagebanner" style="padding-right: 15px;">
                        <cong:td style="width:40%">
                            <div style="float:left">
                                <c:if test="${lclQuoteCommodityForm.noOfPages>0}">
                                    <div style="float:left">
                                        <c:choose>
                                            <c:when test="${lclQuoteCommodityForm.totalPageSize>lclQuoteCommodityForm.noOfRecords}">
                                                ${lclQuoteCommodityForm.pageNo} out of ${lclQuoteCommodityForm.noOfPages} Tariff Details displayed.
                                            </c:when>
                                            <c:when test="${lclQuoteCommodityForm.noOfRecords>1}">${lclQuoteCommodityForm.noOfRecords} Tariff Details displayed.</c:when>
                                            <c:otherwise>1 Tariff displayed.</c:otherwise>
                                        </c:choose>
                                    </div>
                                    <c:if test="${lclQuoteCommodityForm.noOfPages>1 && lclQuoteCommodityForm.pageNo>1}">
                                        <a title="First page" href="javascript: gotoPage('1')">
                                            <img alt="First" src="${path}/images/first.png" border="0"/>
                                        </a>
                                        <a title="Previous page" href="javascript: gotoPage('${lclQuoteCommodityForm.pageNo-1}')">
                                            <img alt="Previous" src="${path}/images/prev.png" border="0"/>
                                        </a>
                                    </c:if>
                                    <c:if test="${lclQuoteCommodityForm.noOfPages>lclQuoteCommodityForm.pageNo}">
                                        <a title="Next page" href="javascript: gotoPage('${lclQuoteCommodityForm.pageNo+1}')">
                                            <img alt="First" src="${path}/images/next.png" border="0"/>
                                        </a>
                                        <a title="Last page" href="javascript: gotoPage('${lclQuoteCommodityForm.noOfPages}')">
                                            <img alt="Previous" src="${path}/images/last.png" border="0"/>
                                        </a>
                                    </c:if>
                                </c:if>
                            </div>
                        </cong:td>
                        <input type="hidden" name="methodName" id="methodName"/>
                        <cong:hidden name="currentPageSize" id="currentPageSize"/>
                        <cong:hidden name="pageNo" id="pageNo"/>
                        <cong:hidden name="noOfPages" id="noOfPages"/>
                        <cong:hidden name="totalPageSize" id="totalPageSize"/>
                        <cong:hidden name="noOfRecords" id="noOfRecords"/>
                        <input type="hidden" name="hazmat" id="hazmat" value="${lclQuotePiece.getHazmat}"/>
                        <cong:td styleClass="td" width="5%">Tariff</cong:td>
                        <cong:td width="20%"><cong:text name="tariff" id="tariff" styleClass="textlabelsBoldForTextBox" value="${tariff}"/></cong:td>
                        <cong:td width="15"><input type="button" class="button-style1" value="Search Tariff" onclick="getTarrifDetails('displayTariffDetails')"/></cong:td>
                        <cong:td width="20%"><input type="button" class="button-style1" value="Select" onclick="submitTariff()"/></cong:td>
                    </div>
                </cong:tr>
                <cong:tr>
                    <c:choose>
                        <c:when test="${not empty commodityTypeList}">
                            <display:table  name="${commodityTypeList}" id="commObj" class="dataTable" sort="list" requestURI="/lclQuoteCommodity.do" >
                                <display:column title="">
                                    <input type="radio" name="tarriff" style="width:8px;height: 8px" id="tarriff" onclick="fillTariffDetails('${commObj.descEn}','${commObj.code}','${commObj.id}','${commObj.hazmat}')"/>
                                </display:column>
                                <display:column title="Tariff" >
                                    <span style="font-size: 8px">${commObj.descEn}</span>
                                </display:column>
                                <display:column title="Tariff#">
                                    <span style="font-size: 8px">${commObj.code}</span>
                                </display:column>
                            </display:table>
                        </c:when>
                        <c:otherwise>No Tariff Detail found</c:otherwise>
                    </c:choose>
                </cong:tr>
            </cong:table>
        </cong:form>
        <script>
            $(document).ready(function(){
                $('#orgId').val(parent.$('#origin').val());
                $('#destId').val(parent.$('#destination').val());
            });
            function getTarrifDetails(methodName){
                $('#pageNo').val('1');
                $('#methodName').val(methodName);
                $('#lclQuoteCommodityForm').submit();
            }

            function gotoPage(pageNo){
                $('#orgId').val(parent.$('#origin').val());
                $('#destId').val(parent.$('#destination').val());
                document.getElementById("pageNo").value = pageNo;
                $('#methodName').val("displayTariffDetails");
                document.lclQuoteCommodityForm.submit();
            }

            function gotoSelectedPage(){
                document.getElementById("pageNo").value =  document.getElementById("selectedPageNo").value;
                $('#methodName').val("displayTariffDetails");
                document.lclQuoteCommodityForm.submit();
            }

            function submitTariff()
            {
                var chks = document.getElementsByName('tarriff');
                var hasChecked = false;
                for (var i = 0; i < chks.length; i++)
                {
                    if (chks[i].checked)
                    {
                        hasChecked = true;
                        parent.$.fn.colorbox.close();
                        break;
                    }
                }
                if (hasChecked == false)
                {
                    sampleAlert("Please select Tariff.");
                    return false;
                }
                return true;
            }

            function fillTariffDetails(desc,code,id,hazmat){
                parent.$('#commodityType').val(desc)
                parent.$('#commodityTypeId').val(id)
                parent.$('#commodityNo').val(code)
                if(hazmat=='true'){
                    parent.$('#hazmatY').attr("checked",true);
                }else{
                    parent.$('#hazmatN').attr("checked",true);
                }
            }
        </script>
    </body>
</html>



