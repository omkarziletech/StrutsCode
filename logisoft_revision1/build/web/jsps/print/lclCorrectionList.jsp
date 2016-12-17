<%-- 
    Document   : /jsps/print/lclCorrectionList.jsp
    Created on : Apr 19, 2016, 10:45:27 AM
    Author     : Mei
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
    <c:set var="index" value="1"/>
    <c:if test="${!empty lclCorrectionNoticeBeanList && fn:length(lclCorrectionNoticeBeanList)>0}">
        <div id="divtablesty11" style="border:thin;overflow:auto;height:80%;">
            <display:table  name="${lclCorrectionNoticeBeanList}" class="displaytagstyle" id="lclCorrectionNoticeBean">
                <display:setProperty name="paging.banner.placement" ><span style="display:none;"></span></display:setProperty>
                <display:column title="Bol ID" >${lclCorrectionNoticeBean.blNo}</display:column>
                <display:column title="CN#">${lclCorrectionNoticeBean.noticeNo}</display:column>
                <display:column title="DATE">${lclCorrectionNoticeBean.noticeDate}</display:column>
                <display:column title="CorrectionType">${lclCorrectionNoticeBean.correctionTypeValue}</display:column>
                <display:column title="Select">
                    <span style="padding-left: 7px;">
                        <input type="radio" name="printRadio" value="${lclCorrectionNoticeBean.noticeNo}"
                               ${index==lclPrintFaxRadioIndex ? 'checked' :''}
                               onclick="resetLclBlCorrectionFile('${lclCorrectionNoticeBean.correctedId}','${lclCorrectionNoticeBean.noticeNo}', '${index}')"/>
                    </span></display:column>
                <c:set value="${index+1}" var="index"/>
            </display:table>
            <table width="100%" border="0">
                <tr >
                    <td  class="textlabelsBold" align="right">Non Corrected LclBL</td>
                    <td align="center" style="padding-right: 10px;">
                        <input type="radio" name="printRadio"
                               ${lclPrintFaxRadioIndex==0 ? 'checked' :''} onclick="resetLclBlCorrectionFile('0','0', '0')" />
                    </td>
                </tr>
            </table>
            <br><br>
        </div>
    </c:if>
</body>
</html>
