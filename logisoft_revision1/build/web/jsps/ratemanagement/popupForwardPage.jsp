<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

if(request.getAttribute("airrate")!=null && request.getAttribute("airrate").equals("addairrate"))
{
%>
	<script>
	    parent.parent.disabled("20");
		parent.parent.GB_hide();
		reload_on_close = true;
	</script>
<%
}
else if(request.getAttribute("retailrate")!=null && request.getAttribute("retailrate").equals("addretailrate"))
		{
%>
		<script>
		parent.parent.getAddRetailRate();
		parent.parent.GB_hide();
		reload_on_close = true;
		</script>
<%
		}
else if(request.getAttribute("lclcoloadrate")!=null && request.getAttribute("lclcoloadrate").equals("addlclcoloadrate"))
{
%>
	<script>
	         parent.parent.getLCLCoload();
	         parent.parent.GB_hide();
	         reload_on_close = true;
	</script>
<%
}
else if(request.getAttribute("ftfrate")!=null && request.getAttribute("ftfrate").equals("addftfrate"))
{

%>
	<script>
	
	     parent.parent.getFTF();
	     parent.parent.GB_hide();
	     
	</script>
<%
}
else if(request.getAttribute("sendfclcontrol")!=null && request.getAttribute("sendfclcontrol").equals("sendfclcontrol"))
{
%>
	<script>

		parent.parent.getFclCurrent();
		parent.parent.GB_hide();
		reload_on_close = fasle;
	</script>
<%
}
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  <%@include file="../includes/resources.jsp" %>
    <base href="<%=basePath%>">
    
    <title>My JSP 'a.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    This is my JSP page. <br>
  </body>
</html>
