<%@ page language="java" import="java.util.*,com.gp.cong.struts.LoadLogisoftProperties" pageEncoding="ISO-8859-1"%>
<%
            String path = request.getContextPath();
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
            String address = request.getParameter("address");
//get the map key
            LoadLogisoftProperties logisoftProperties = new LoadLogisoftProperties();
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <base href="<%=basePath%>">
        <title>My JSP 'DojoExample.jsp' starting page</title>
        <meta http-equiv="pragma" content="no-cache">
        <meta http-equiv="cache-control" content="no-cache">
        <meta http-equiv="expires" content="0">
        <meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
        <meta http-equiv="description" content="This is my page">
        <%--	<script type="text/javascript" src="http://o.aolcdn.com/iamalpha/.resource/jssdk/dojo-0.2.2/dojo.js"></script>--%>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=logisoftProperties.getProperty("googleMapKey")%>&sensor=true"></script>
        <script type="text/javascript">
            var map = null;
            var geocoder = null;
            function initialize(val) {
                geocoder = new google.maps.Geocoder();
                geocoder.geocode( { 'address':val+",USA"}, function(results, status) {
                    if (status == google.maps.GeocoderStatus.OK) {
                        var mapOptions = {
                            zoom: 4,
                            center: results[0].geometry.location,
                            mapTypeId: google.maps.MapTypeId.ROADMAP
                        }
                        map = new google.maps.Map(document.getElementById("map_canvas"),mapOptions);
                        var marker = new google.maps.Marker({
                            position: new google.maps.LatLng(results[0].geometry.location.lat(),results[0].geometry.location.lng()),
                            title:val
                        });
                        marker.setMap(map);
                    }else {
                        alert("Unable to Find ${countryName}");
                    }
                });
            }
        </script>
    </head>
    <body onload="initialize('<%=address%>')" onunload="GUnload()" style="font-family: Arial;border: 0 none;">
        <div id="map_canvas" style="width: 600px; height: 400px"></div>
    </body>
</html>
