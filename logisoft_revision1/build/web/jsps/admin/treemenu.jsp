<%@ page import="com.gp.cong.logisoft.domain.User,java.util.Map,java.util.HashMap,com.gp.cong.logisoft.domain.Role,com.gp.cong.logisoft.domain.Item"%>

<%@ page language="java" import="java.util.*"%>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-bean" prefix="bean" %>
<%@ taglib uri="http://jakarta.apache.org/struts/tags-html" prefix="html" %>
<%
    User user = null;
    String path = request.getContextPath();
    if (session.getAttribute("loginuser") != null) {
        user = (User) session.getAttribute("loginuser");
    }
    com.gp.cong.logisoft.util.GenerateMenu generateMenu = new com.gp.cong.logisoft.util.GenerateMenu();
    Map<Item, List> menuMap = generateMenu.getMenuStructure();

%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
        <title>Untitled Document</title>
        <%@include file="../includes/baseResources.jsp" %>
        <%@include file="../includes/jspVariables.jsp" %>

        <script src="<%=path%>/js/accordion/jquery-1.2.1.min.js" type="text/javascript"></script>
        <script src="<%=path%>/js/accordion/menu.js" type="text/javascript"></script>

        <script language="javascript">

            function retainActiveLinks(hrefObj) {
                aObj = document.getElementById('menu').getElementsByTagName('a');
  
                for(i=0;i<aObj.length;i++) {
                    if(aObj[i].href.indexOf('#')==-1){
                        aObj[i].style.background=ACCORDION_CELL_COLOR;
                        aObj[i].style.color='#000';
                        aObj[i].style.borderLeft='0px';
                    }
                }
  
                hrefObj.style.background=ACCORDION_SELECTED_COLOR;
                hrefObj.style.borderLeft='5px '+ACCORDION_SELECTED_SIDE_BORDER_COLOR+' solid';
                hrefObj.style.color='#fff';
                hrefObj.style.paddingLeft='15px';  
                parent.setBreadCrumb(hrefObj.name);
            }
        </script>
        <%@include file="../includes/baseResources.jsp" %>
    </head>

    <BODY topmargin="0" marginheight="0" class="whitebackgrnd">

        <ul id="menu">

            <%
                for (Iterator iter = menuMap.keySet().iterator(); iter.hasNext();) {
                    Item element = (Item) iter.next();
            %>
            <li>
                <a href="#"><%=element.getItemDesc()%></a>
                <%
                        if (menuMap.get(element) != null && menuMap.get(element).size() > 0) {
                            out.println("<ul>");
                            for (Iterator iter1 = menuMap.get(element).iterator(); iter1.hasNext();) {
                                Item childItem = (Item) iter1.next();
                                if (!user.getRole().getRoleDesc().equals("Admin")) {
                                    if (childItem.getItemDesc().equals("Menu Management")) {
                                    } else {
                                        Item firstChildItem = generateMenu.getFirstItemFromParent(childItem);

                                        if (firstChildItem != null && firstChildItem.getId() != null) {
                                            out.println("<li ><a onClick=\"retainActiveLinks(this);\" href='" + path + "/jsps/Tab.jsp?folderId=" + childItem.getItemId() + "' name='" + element.getItemDesc() + " >> " + childItem.getItemDesc() + "' target='tabframe'>" + childItem.getItemDesc() + "</a></li>");
                                        }
                                    }
                                } else {
                                    Item firstChildItem = generateMenu.getFirstItemFromParent(childItem);

                                    if (firstChildItem != null && firstChildItem.getId() != null) {
                                        out.println("<li ><a onClick=\"retainActiveLinks(this);\" href='" + path + "/jsps/Tab.jsp?folderId=" + childItem.getItemId() + "' name='" + element.getItemDesc() + " >> " + childItem.getItemDesc() + "' target='tabframe'>" + childItem.getItemDesc() + "</a></li>");
                                    }
                                }
                            }
                            out.println("</ul>");
                        }
                        out.println("</li>");
                    }
                %>
        </ul>
    </body>
    <%@include file="../includes/baseResourcesForJS.jsp" %>
</html>
