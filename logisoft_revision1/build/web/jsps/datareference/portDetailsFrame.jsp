<%@ page language="java"  import="com.gp.cong.logisoft.util.DBUtil"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean"%> 
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html"%>
<%@ taglib uri="/WEB-INF/displaytag-12.tld"  prefix="display"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
DBUtil dbUtil=new DBUtil();
	String portDetailsPath=path+"/jsps/datareference/NewPortDetails.jsp";
	String portTabDetailsPath=path+"/jsps/portsTabs.jsp";


%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Frameset//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-frameset.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
<title>Port</title>
</head>

<frameset rows="50,258" cols="" framespacing="0"" frameborder="NO" border="0">
  <frame src="<%=portDetailsPath%>" name="topFrame" id="topFrame" title="top" scrolling="no"/>
  <frame src="<%=portTabDetailsPath%>" name="mainFrame" id="mainFrame" title="bottom" scrolling="no" />
</frameset>
<noframes><body>
</body>
</noframes></html>