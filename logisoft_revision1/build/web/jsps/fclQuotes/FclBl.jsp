
<html>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

if(request.getAttribute("requestObjectVAlue")!=null){

  %>
  <script type="text/javascript">
		window.top.tabframe.location='<%=path%>/jsps/Tab.jsp?<%=request.getAttribute("requestObjectVAlue")%>&editFCL=<%=request.getAttribute("editFCL")%>';
  </script>
  <%
  }
%>

<head>

</head>
<body class="whitebackgrnd" >
<div align="left"><font color="#0000ff" size="3"><b> Search   Through  'SearchByFileNumber'   Tab</b></font></div>
</body>

</html>

