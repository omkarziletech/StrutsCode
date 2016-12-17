
<%--
    Document   : lclEdiData.jsp
    Created on : Oct 8, 2010, 4:25:08 PM
    Author     : Logiware
--%>

<%@page import="javax.swing.JFileChooser"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@ taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-13.tld" prefix="display"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<table width="100%"
       style="background-image: url(${path}/js/greybox/header_bg.gif);border-bottom: 2px solid #AAAAAA;">
    <tbody>
        <tr>
            <td class="lightBoxHeader">
				Do you want to apply this data in LCl Bl from EDI System ?
            </td>
            <td>
                <div style="vertical-align: top">
                    <a id="lightBoxClose" href="javascript: closeLclEdiData();">
                        <img src="/logisoft/js/greybox/w_close.gif" title="Close" style="border: none;">Close
                    </a>
                </div>
            </td>
        </tr>
    </tbody>
</table>
<span id="doorlclEdiDataDisplay" style="display:none"></span>
<table cellpadding="2" cellspacing="2" style="width: 98%">
    <tr class="textlabelsBold" style="background:#DCDCDC">
        <td align="left">&nbsp;  <input type="checkbox" name="checkAll" id="checkAll" onclick="checkAndUnceckAll('checkAll','lclEdiData');">ALL

            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="button" class="buttonStyleNew" name="Continue" value="Continue" onclick="continuelclEdiData('${path}','${fileNumberId}','BL')">
        </td>
    </tr>
</table>
<div id="lclEdiData" style="width: 98%;height:305px;overflow:scroll">
    <table align="left">
        <tr>
            <c:choose>
                <c:when test="${not empty blOrigin}">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">BL Origin:</span></td>
                    <td>  ${blOrigin}  </td>
                </tr>
            </c:when>
            <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty portOfLoad }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Port Of Loading :</span></td>
                    <td>${portOfLoad}</td>
                </tr>
            </c:when>
            <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty placeOfDischarge }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Port Of Discharge :</span> </td>
                    <td>${placeOfDischarge}</td>
                </tr>
            </c:when>
            <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty shipperName }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Shipper : </span> </td>
                    <td>${shipperName}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty forwarderName }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style=" color:black;font-weight: bolder">Forwarder : </span> </td>
                    <td>${forwarderName}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty consigneeName }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Consignee : </span></td>
                    <td>${consigneeName}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty notifyName }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style=" color:black;font-weight: bolder">Notify : </span></td>
                    <td>${notifyName}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>


        <c:choose>
            <c:when test="${not empty exportRefNo }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style=" color:black; font-weight: bolder">Export Ref No : </span></td>
                    <td>${exportRefNo}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty frtfwdRefNo }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Forwarder Ref No  :</span> </td>
                    <td>${frtfwdRefNo}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty shipperRefNo }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Shipper Ref No :</span></td>
                    <td>${shipperRefNo}</td></tr>
                </c:when>
                <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>
        <c:choose>
            <c:when test="${not empty goodsDesc }">
                <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value=""><span style="color:black; font-weight: bolder">Commodity :</span></td>
                    <td>
                        <display:table  name="${packSet}" id="commObj" class="dataTable" sort="list" requestURI="/lclBlCommodity.do" >
                            <display:column title="Commodity ">
                                ${commObj.commodity}
                            </display:column>
                            <display:column title="No Of Piece ">
                                ${commObj.noOfPackage}
                            </display:column>
                            <%--
                            <display:column title="Gross Volume">
                                ${commObj.goodgrossVolume}
                            </display:column>
                            <display:column title="Gross Volume type">
                                ${commObj.goodgrossVolumeType}
                            </display:column>
                            <display:column title="Gross Weight">
                                ${commObj.goodgrossWeight}
                            </display:column>
                            <display:column title="Gross Weight Type">
                                ${commObj.goodgrossWeightType}
                            </display:column>
                            --%>

                            <display:column title="Commodity Description">
                                ${commObj.goodsDesc}
                            </display:column>
                            <display:column title="Marks & No">
                                ${commObj.marksAndNo}
                            </display:column>
                        </display:table>
                    </td>
                </tr>
            </c:when>
            <c:otherwise>
                <input type="hidden" class="lclEdiData" name="lclEdiData"/>
            </c:otherwise>
        </c:choose>



        <%--
       <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value="${goodsDesc}">MARKS & NUMBERS</td></tr>
       <tr class="textlabelsBold"> <td align="left"><input type="checkbox" class="lclEdiData" name="lclEdiData" value="${marksAndNo}">COMMODITY DESCRIPTION</td></tr>
        --%>
        </tr>
    </table>
</div>
