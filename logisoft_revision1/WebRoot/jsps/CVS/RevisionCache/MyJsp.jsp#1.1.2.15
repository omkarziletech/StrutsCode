<%--<%@ page language="java" import="java.util.*" pageEncoding="ISO-8859-1"%>
<jsp:directive.page import="com.gp.cong.common.GenerateFileNumber"/>
 <%@include file="../fragment/fclcontainer.jspf"  %>
 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
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
	<script language="javascript" src="<%=path%>/js/dojo/dojo.js"></script>
	<script type="text/javascript" src="http://o.aolcdn.com/iamalpha/.resource/jssdk/dojo-0.2.2/dojo.js"></script>
	<script type="text/javascript">
   	dojo.require("dojo.dnd.*");
    dojo.require("dojo.event.*");
    start = function(){
			initKitchen();
			}
	dojo.addOnLoad(start);
   function initKitchen() {
      // "pan" matches the id for the <div> tag with id="pan" below
      // The [] around "dest" are required, for reasons we'll see later
      new dojo.dnd.HtmlDropTarget(dojo.byId("pan"), ["dest"]);
      alert("AAA");
      new dojo.dnd.HtmlDropTarget(dojo.byId("pot"), ["dest"]);
      alert("AAA");
      new dojo.dnd.HtmlDropTarget(dojo.byId("oven"), ["dest"]);
      alert("AAA");
      // "dest" matches the "dest" in the DropTarget's above.
      new dojo.dnd.HtmlDragSource(dojo.byId("egg"), "dest");      
      alert("AAA");
      new dojo.dnd.HtmlDragSource(dojo.byId("leg"), "dest");
   }
		
   //dojo.addOnLoad("start");
</script>
	
  </head>
  
    <body>
    <%GenerateFileNumber generateFileNumber = new GenerateFileNumber("one");
   out.print( generateFileNumber.getFileNumber());
    GenerateFileNumber generateFileNumber2 = new GenerateFileNumber("two");
    out.print( generateFileNumber2.getFileNumber());
      %>
   <table border="0" width="100%">
   <tr>
   <td>Name</td><td>
    <select name="sel3" size="1"  disabled  style="font-weight:500" >
  <option value=1>Select</option>
	<OPTION VALUE="">2</option>
	<OPTION VALUE="">3</option>
  </SELECT>
   <input type="text" value="Name" style="color:black;"/>
   
   </td>
   </tr>
   </table>

  </body>
</html>
--%>

<html><body>
<head>
         <script type='text/javascript' src='/logisoft/dwr/engine.js'></script>
          <script type='text/javascript' src='/logisoft/dwr/interface/FclBlBC.js'></script>
          <script type='text/javascript' >
          function getDate(){
                    return false;
                    alert('data');
                    FclBlBC.getFclBLObjectByBolid('11-BRGOB-04-255016', function(data){
                        alert(data);
                    });
          }
          </script></head>
<body onload="getDate()">
hiooooooooo</body>
</html>