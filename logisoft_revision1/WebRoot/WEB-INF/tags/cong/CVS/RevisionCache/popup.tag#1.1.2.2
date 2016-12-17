<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : Lakshmi Narayanan
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib tagdir="/WEB-INF/tags/cong" prefix="cong" %>
<%@tag description="put the tag description here" pageEncoding="UTF-8"%>
<%@attribute name="title" required="true"%>
<%@attribute name="tooltip"%>
<%@attribute name="url" required="true"%>
<%@attribute name="height"%>
<%@attribute name="width"%>
<%@attribute name="imgHeight"%>
<%@attribute name="imgWidth"%>
<%@attribute name="name"%>
<%@attribute name="label"%>
<%@attribute name="value"%>
<%@attribute name="type"%>
<%@attribute name="src"%>
<%@attribute name="styleId"%>
<%@attribute name="styleClass"%>
<%@attribute name="callBack"%>
<%@attribute name="callFrom"%>

<c:if test="${popup != 'popup'}">
    <link rel="stylesheet" href="${path}/js/lightbox/lightbox.css" type="text/css" media="all">
    <script type="text/javascript" src="${path}/js/lightbox/lightbox.v2.3.jquery.js"></script>
    <script type="text/javascript">
        $(document).ready(function(){
            Lightbox.initialize({   color:'black',
                dir : '${path}/js/lightbox/images',
                moveDuration : 1,
                resizeDuration : 1
            });
        });
    </script>
</c:if>

<c:if test="${not empty callFrom}">
    <c:set var="callFrom" value="${callFrom}."/>
</c:if>

<c:if test="${empty label}">
    <c:set var="label" value="${title}"/>
</c:if>
<c:if test="${empty width}">
    <c:set var="width" value="720"/>
</c:if>
<c:if test="${empty height}">
    <c:set var="height" value="400"/>
</c:if>
    <c:choose>
        <c:when test="${fn:contains(url,'?')}">
            <c:set var="url" value="${url}&TB_iframe&height=${height}&width=${width}"/>
        </c:when>
        <c:otherwise>
            <c:set var="url" value="${url}?TB_iframe&height=${height}&width=${width}"/>
        </c:otherwise>
    </c:choose>
<c:choose>
    <c:when test="${type=='link'}">
        <a href="#" onclick="${callFrom}Lightbox.showPopUp('${title}', '${url}', 'sexylightbox', '','${callBack}')" class="${styleClass}" title="${tooltip}">${label}</a>
    </c:when>
    <c:when test="${type=='img'}">
        <c:if test="${empty src}">
            <cong:prompt type="warning" text="attribute src is required for popup of type img"/>
        </c:if>
        <img style="cursor:pointer"  title="${tooltip}" src="${src}" height="${imgHeight}" width="${imgWidth}" class="${styleClass}" alt="${label}" id="${styleId}" onclick="${callFrom}Lightbox.showPopUp('${title}', '${url}', 'sexylightbox', '','${callBack}')"/>
    </c:when>
    <c:when test="${type=='radio'}">
        <cong:radio  name="${name}" value="${value}" styleClass="${styleClass}"  id="${styleId}" onclick="${callFrom}Lightbox.showPopUp('${title}', '${url}', 'sexylightbox', '','${callBack}')" container="NULL"/>
    </c:when>
    <c:otherwise>
        <input type="button"  title="${tooltip}" value="${label}" class="${styleClass}"  id="${styleId}" onclick="${callFrom}Lightbox.showPopUp('${title}', '${url}', 'sexylightbox', '','${callBack}')"/>
    </c:otherwise>
</c:choose>
<c:set var="popup" value="popup" scope="request"/>
