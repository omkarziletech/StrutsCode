<%@ page language="java" import="java.util.*,com.gp.cong.struts.LoadLogisoftProperties" pageEncoding="ISO-8859-1"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
//get the map key
LoadLogisoftProperties logisoftProperties = new LoadLogisoftProperties();
List<String> cityList = (List<String>)session.getAttribute(request.getParameter("sessionMapKey"));
String destails = (String)session.getAttribute(request.getParameter("sessionMapKey")+"_collapse");
String expandDestails = (String)session.getAttribute(request.getParameter("sessionMapKey")+"_expand");
String normalDestails = (String)session.getAttribute(request.getParameter("sessionMapKey")+"_normal");
String doorOrigin = request.getParameter("doorOrigin");
String zip = request.getParameter("zip");
String requestURL = request.getRequestURL().toString();
String url = requestURL.substring(0,requestURL.indexOf("logisoft")+8);
%>
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

<%--	<script type="text/javascript" src="http://o.aolcdn.com/iamalpha/.resource/jssdk/dojo-0.2.2/dojo.js"></script>--%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<script language="javascript" src="${path}/js/common.js"></script>
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=<%=logisoftProperties.getProperty("googleMapKey")%>&sensor=true"></script>   <script type="text/javascript">

    var map = null;
    var geocoder = null;
    var glatlang = null;
    var directionsService = null;
    var glatlan = null;

       function initialize(val) { 
        geocoder = new google.maps.Geocoder();
        directionsService = new google.maps.DirectionsService();
            geocoder.geocode( { 'address':'<%=zip%>'+",USA"}, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) { 
                glatlang = results[0].geometry.location;
                var mapOptions = {
                    zoom: 4,
                    center: results[0].geometry.location,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                }
            map = new google.maps.Map(document.getElementById("map_canvas"),mapOptions);
                <%if(null != zip && !zip.trim().equals("")) {%>
                geocoder.geocode({'address':'<%=zip%>'},function(point){
                glatlan = point;
                var marker = new google.maps.Marker({
                    position: new google.maps.LatLng(results[0].geometry.location.lat(),results[0].geometry.location.lng()),
                    icon:'<%=url%>'+'/img/icons/door_origin.png',
                    title:"<%=doorOrigin%>"
                });
                marker.setMap(map);
                showAddress(val);
                });
             <%} else {%> 
             showAddress(val);
       <%}%>
       }
   });
}
     
    function showAddress(val) { 
      var city = null;
      if (geocoder) { 
        <% for(Object city : cityList) {%>
        city = null;
            city = "<%=city%>";
        if(city.indexOf("(") > 1) { 
          var  _city = city.substring(0, city.indexOf("(")-1)+city.substring(city.indexOf(")")+1, city.length-1);
          city = _city;
        }
        geocoder.geocode( { 'address':city}, function(results) {
            var infowindow = new google.maps.InfoWindow({
            content: document.getElementById("<%=city%>"+"_collapse")
             });
            if (!results) {
            } else {
              if(glatlan != null) {
                  var distance;
                  var request = {
                        origin:""+glatlang,
                        destination:""+results[0].geometry.location,
                        travelMode: google.maps.DirectionsTravelMode.DRIVING
                    };
                   directionsService.route(request, function(response, status){
                   if (status == google.maps.DirectionsStatus.OK){ 
                   distance = response.routes[0].legs[0].distance.text; 
                   var marker = new google.maps.Marker({
                   position: new google.maps.LatLng(results[0].geometry.location.lat(),results[0].geometry.location.lng()),
                   title:"<%=city%> - "+distance+"("+response.routes[0].legs[0].duration.text+")"
                   });
                   marker.setMap(map);
                   google.maps.event.addListener(marker, 'click', function() {
                   infowindow.open(map,marker);
                   });
                   }
                   });
                   }else {
                   var marker = new google.maps.Marker({
                   position: new google.maps.LatLng(results[0].geometry.location.lat(),results[0].geometry.location.lng()),
                   title:"<%=city%>"
                   });
                   marker.setMap(map);
                   map.setCenter(marker.getPosition());
                   google.maps.event.addListener(marker, 'click', function() {
                   infowindow.open(map,marker);
                   });
                }
            }
        });
                <%}%>
        }
    }

    function getDrivingDirections(location1, location2) {
		
		var request = {
			origin:location1,
			destination:location2,
			travelMode: google.maps.DirectionsTravelMode.DRIVING
		};
		directionsService.route(request, function(response, status)
		{
			if (status == google.maps.DirectionsStatus.OK){
				distance = response.routes[0].legs[0].distance.text;
			}
		});
    }
    function selectUnits(val) {
        var unit = document.getElementById(val).value;
        var units = unit.split(",",unit.length);
        var unitType = document.getElementsByName("unitType");
        for(i=0; i<unitType.length; i++) {
            unitType[i].checked=false;
            var selected = false;
            for(j=0; j<units.length;j++) {
                if(unitType[i].value==units[j]) {
                    unitType[i].disabled = false;
                    unitType[i].checked = true;
                    selected = true;
                    break;
                }
            }
            if(!selected) {
                unitType[i].checked = false;
                unitType[i].disabled = true;
            }
        }
    }
    </script>
  </head>
  </head>
  <body onload='initialize("${param.cities}")' onunload="GUnload()" style="font-family: Arial;border: 0 none;">
     <div id="map_canvas" style="width:100%; height: 100%"></div>
     <div id="rates" style="display:none">
         <%=destails%>
         <%=expandDestails%>
         <%=normalDestails%>
     </div>
  </body>
  <script type="text/javascript"
		src="${path}/js/sweet-titles-tooltip/addEvent.js"></script>
	<script type="text/javascript"
		src="${path}/js/sweet-titles-tooltip/sweetTitles.js"></script>
	<script type="text/javascript"
		src="${path}/js/tablesorter/jquery-latest.js"></script>
	<script type="text/javascript"
		src="${path}/js/tablesorter/jquery.tablesorter.js"></script>
	<script type="text/javascript"
		src="${path}/js/tablesorter/jquery.tablesorter.pager.js"></script>
        <link rel="stylesheet" type="text/css" href="${path}/css/sweetTitles.css" />
</html>
