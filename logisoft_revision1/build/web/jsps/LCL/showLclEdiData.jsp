<%-- 
    Document   : showLclEdiData
    Created on : 16 Jul, 2016, 11:26:56 PM
    Author     :PAL RAJ
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%@include file="init.jsp" %>
<%@include file="/jsps/includes/baseResources.jsp" %>
<%@taglib uri="/WEB-INF/fn.tld" prefix="fn" %>
<%@include file="/jsps/preloader.jsp" %>
<%@taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<c:set var="path" value="${pageContext.request.contextPath}"></c:set>
<cong:javascript  src="${path}/jsps/LCL/js/lclBooking.js"/>
<html>
    <body>
        <div>
            <table class="display-table" cellpadding = "3" cellspacing="2">
                <thead>
                    <tr>
                        <th bgcolor="#D3D3D3">Export Reference</th>
                        <th bgcolor="#D3D3D3">Marks & Numbers</th>
                        <th bgcolor="#D3D3D3">Commodity Description</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td bgcolor="#ADD8E6">${exportRefNo}</td>
                        <td bgcolor="#ADD8E6">${marksAndNo}</td>
                        <td bgcolor="#ADD8E6">${commodityDesc}</td>
                    </tr>
                </tbody>
            </table>
        </div>
        <div>
            <cong:table width="100%">
                <cong:tr >
                    <cong:td styleClass="lclEdiMessage">
                        <span>Do you want to apply the information from the SI, to the BL ?</span>
                        <input type="button" class="lclEdiYesButton" value="Yes" onclick="lclEdiAndKnDataconvertToBL('${path}', '${fileNumberId}', 'BL', 'Yes')"/>
                        <input type="button" class="lclEdiNOButton" value="No" onclick="lclEdiAndKnDataconvertToBL('${path}', '${fileNumberId}', 'BL', 'No')"/>
                    </cong:td>
                </cong:tr>
            </cong:table>
        </div>
    </body>
</html>