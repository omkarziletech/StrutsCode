<%@ page language="java" import="java.util.*,com.gp.cong.struts.LoadLogisoftProperties" pageEncoding="ISO-8859-1"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
//get the map key
    LoadLogisoftProperties logisoftProperties = new LoadLogisoftProperties();
    List<Object[]> unlocList = (List<Object[]>) request.getAttribute("destinationList");
%>
<%@include file="init.jsp" %>
<%@include file="../includes/jspVariables.jsp"  %>
<%@include file="../includes/baseResources.jsp" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%> 
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

        <c:set var="path" value="${pageContext.request.contextPath}"/>
        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?v=3.exp&key=<%=logisoftProperties.getProperty("googleMapKey")%>&sensor=true"></script>
        <script type="text/javascript">
            var map = null;
            var geocoder = null;
            var STREETVIEW_MAX_DISTANCE = 100;
            function initialize() {
                geocoder = new google.maps.Geocoder();
                <c:choose>
                    <c:when test="${not empty address}">
                        geocoder.geocode({'address': "${address}"}, function (results, status) {
                                    if (status === google.maps.GeocoderStatus.OK) {
                                        var latlong = new google.maps.LatLng(results[0].geometry.location.lat(), results[0].geometry.location.lng());
                                        var mapOptions = {
                                            zoom: 4,
                                            center: results[0].geometry.location,
                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                        }
                                        map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
                                        var marker = new google.maps.Marker({
                                            position: latlong,
                                            title: "${address}"
                                        });
                                        //Code for Street View******************************
                                        var panoramaOptions = {
                                            position: latlong,
                                            pov: {
                                                heading: 34,
                                                pitch: 10,
                                                zoom: 1
                                            }
                                        };
                                        var panorama = new google.maps.StreetViewPanorama(document.getElementById("pano"), panoramaOptions);
                                        var streetViewService = new google.maps.StreetViewService();
                                        streetViewService.getPanoramaByLocation(latlong, STREETVIEW_MAX_DISTANCE, function (panorama, status) {
                                            if (status === google.maps.StreetViewStatus.OK) {
                                            } else {
                                                $('#pano').css("display", "none");

                                            }
                                        });
                                        map.setStreetView(panorama);
                                        //**************************************************
                                        marker.setMap(map);
                                    } else {
                                        geocoder.geocode({'address': "${locationName},${countryName}"}, function (results, status) {
                                                    if (status === google.maps.GeocoderStatus.OK) {
                                                        var mapOptions = {
                                                            zoom: 4,
                                                            center: results[0].geometry.location,
                                                            mapTypeId: google.maps.MapTypeId.ROADMAP
                                                        }
                                                        map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
                                                        <%for (Object[] obj : unlocList) {%>
                                                            var latitude;
                                                            var longitude;
                                                            var count = <%=unlocList.size()%>
                                                            if (count === '1') {
                                                                latitude = results[0].geometry.location.lat();
                                                                longitude = results[0].geometry.location.lng();
                                                            } else {
                                                                latitude =<%=obj[3]%>;
                                                                longitude =<%=obj[4]%>;
                                                            }
                                                            var marker = new google.maps.Marker({
                                                                position: new google.maps.LatLng(latitude, longitude),
                                                                title: "<%=obj[0]%>/<%=obj[1]%>(<%=obj[2]%>)"
                                                            });
                                                            //Code for Street View******************************
                                                            var panoramaOptions = {
                                                                position: new google.maps.LatLng(latitude, longitude),
                                                                pov: {
                                                                    heading: 34,
                                                                    pitch: 10,
                                                                    zoom: 1
                                                                }
                                                            };
                                                            var panorama = new google.maps.StreetViewPanorama(document.getElementById("pano"), panoramaOptions);
                                                            var streetViewService = new google.maps.StreetViewService();
                                                            var latLng = new google.maps.LatLng(latitude, longitude);
                                                            streetViewService.getPanoramaByLocation(latLng, STREETVIEW_MAX_DISTANCE, function (panorama, status) {
                                                                if (status === google.maps.StreetViewStatus.OK) {
                                                                } else {
                                                                    $('#pano').css("display", "none");

                                                                }
                                                            });
                                                            map.setStreetView(panorama);
                                                            //**************************************************
                                                            marker.setMap(map);
                                                        <%}%>
                                                    } else {
                                                        alert("Unable to Find ${locationName} ,${countryName}");
                                                    }
                                                });
                                    }
                                });
                    </c:when>
                    <c:otherwise>
                        geocoder.geocode({'address': "${locationName},${countryName}"}, function (results, status) {
                                        if (status === google.maps.GeocoderStatus.OK) {
                                            var mapOptions = {
                                                zoom: 4,
                                                center: results[0].geometry.location,
                                                mapTypeId: google.maps.MapTypeId.ROADMAP
                                            }
                                            map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
                                            <%for (Object[] obj : unlocList) {%>
                                                var latitude;
                                                var longitude;
                                                var count = <%=unlocList.size()%>
                                                if (count === '1') {
                                                    latitude = results[0].geometry.location.lat();
                                                    longitude = results[0].geometry.location.lng();
                                                } else {
                                                    latitude =<%=obj[3]%>;
                                                    longitude =<%=obj[4]%>;
                                                }
                                                var marker = new google.maps.Marker({
                                                    position: new google.maps.LatLng(latitude, longitude),
                                                    title: "<%=obj[0]%>/<%=obj[1]%>(<%=obj[2]%>)"
                                                });
                                                //Code for Street View******************************
                                                var panoramaOptions = {
                                                    position: new google.maps.LatLng(latitude, longitude),
                                                    pov: {
                                                        heading: 34,
                                                        pitch: 10,
                                                        zoom: 1
                                                    }
                                                };
                                                var panorama = new google.maps.StreetViewPanorama(document.getElementById("pano"), panoramaOptions);
                                                var streetViewService = new google.maps.StreetViewService();
                                                var latLng = new google.maps.LatLng(latitude, longitude);
                                                streetViewService.getPanoramaByLocation(latLng, STREETVIEW_MAX_DISTANCE, function (panorama, status) {
                                                    if (status === google.maps.StreetViewStatus.OK) {
                                                    } else {
                                                        $('#pano').css("display", "none");

                                                    }
                                                });
                                                map.setStreetView(panorama);
                                                //**************************************************
                                                marker.setMap(map);
                                            <%}%>
                                        } else {
                                            alert("Unable to Find ${locationName} ,${countryName}");
                                        }
                                    });
                    </c:otherwise>
                </c:choose>
            }
        </script>
    </head>
</head>
<body onload='initialize()' onunload="GUnload()" style="font-family: Arial;border: 0 none;">
    <div id="map_canvas" style="width:100%; height: 100%"></div>
    <div id="pano" style="position:absolute; left:70%; top: 0px; width: 30%; height: 100%;"></div>
</body>
</html>
