<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>

<%@attribute name="autocompleter" type="java.lang.Boolean"%>
<%@attribute name="enableFormatting" type="java.lang.Boolean"%>
<%@attribute name="enableAjaxForm" type="java.lang.Boolean"%>
<%@attribute name="enablePopup" type="java.lang.Boolean"%>
<%@attribute name="checkPageChange" type="java.lang.Boolean"%>
<%@taglib uri="/WEB-INF/c.tld" prefix="c"%>
<%@taglib tagdir="/WEB-INF/tags/cong/" prefix="cong"%>
<c:if test="${autocompleter && autocomplete == null}">
    <link rel="stylesheet" type="text/css" href="/css/jquery.autocomplete.css" />
    <script src="${path}/js/autocomplete/jsapi.js" type="text/javascript" ></script>
    <script src="${path}/js/autocomplete/jquery.autocomplete.js" type="text/javascript" ></script>
    <c:set var="autocomplete" value="autocomplete" scope="request"/>
</c:if>
<c:if test="${enableAjaxForm}">
    <script src="${path}/js/ajax/jquery.form.js" type="text/javascript" ></script>
</c:if>
<c:if test="${checkPageChange}">
    <script type="text/javascript">
        var isPageChanged = false;
        $(document).ready(function(){
            $("input,textarea").change(function(){
                isPageChanged = true;
            });
        });
        
        function checkPageChange(fun,options){
            if(isPageChanged){                
                RegularConfirm("Are you sure you want to abandon the changes?",fun,options);
            }else{
                fun(options);
            }
        }        
        
    </script>
</c:if>

<c:if test="${enableFormatting}">
    <script src="${path}/js/common/currency.js" type="text/javascript" ></script>
    <script>
        $('.currency').currencyFormat();
    </script>
</c:if>

<c:if test="${popup != 'popup' && enablePopup}">
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
    <c:set var="popup" value="popup" scope="request"/>
</c:if>
