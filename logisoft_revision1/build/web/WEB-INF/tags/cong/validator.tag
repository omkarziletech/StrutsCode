<%--
    Document   : sun
    Created on : Jun 4, 2010, 8:09:44 PM
    Author     : sunil
--%>

<%@attribute name="form" required="true"%>
<script src="${path}/js/validation/jquery_002.js" type="text/javascript" ></script>
<script type="text/javascript">
    $.validator.setDefaults({
        //submitHandler: function() { alert("submitted!!!!"); }
    });

    $().ready(function() {        
        $("#${form}").validate();
    });
</script>

<jsp:doBody/>
