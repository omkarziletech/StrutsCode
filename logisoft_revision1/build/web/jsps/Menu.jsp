<%--
    Document   : Menu
    Created on : Sep 21, 2010, 2:50:00 PM
    Author     : Lakshmi Naryanan
--%>
<%@page import="java.util.ArrayList"%>
<%@page import="com.logiware.constants.ItemConstants"%>
<%@page import="com.logiware.bean.ItemBean"%>
<%@page import="java.util.HashMap"%>
<%@page import="com.gp.cong.common.CommonUtils"%>
<%@page import="java.util.Map"%>
<%@page import="com.gp.cong.logisoft.domain.Item"%>
<%@page import="java.util.List"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.ItemDAO"%>
<%
            ItemDAO itemDAO = new ItemDAO();
            Map<Item, Map<Item, Map<Item, Object>>> treeItems = new HashMap<Item, Map<Item, Map<Item, Object>>>();
            List<Item> rootItems = itemDAO.getMenu();
            for (Item root : rootItems) {
                List<Item> subRootItems = new ItemDAO().getSubMenu(root.getItemId());
                Map<Item, Map<Item, Object>> subRoots = new HashMap<Item, Map<Item, Object>>();
                for (Item subRoot : subRootItems) {
                    boolean canCheck = true;
                    if (CommonUtils.isEqualIgnoreCase(subRoot.getItemDesc(), ItemConstants.ADMIN)) {
                        canCheck = false;
                    }
                    if (CommonUtils.isEmpty(subRoot.getProgramName())) {
                        List<Item> parentItems = new ItemDAO().getSubMenu(subRoot.getItemId());
                        Map<Item, Object> childs = new HashMap<Item, Object>();
                        for (Item parent : parentItems) {
                            if (CommonUtils.isEmpty(parent.getProgramName())) {
                                List<Item> childItems = new ItemDAO().getSubMenu(parent.getItemId());
                                List<ItemBean> childItemBeans = new ArrayList<ItemBean>();
                                for (Item childItem : childItems) {
                                    childItemBeans.add(new ItemBean(childItem, canCheck));
                                }
                                childs.put(parent, childItemBeans);
                            } else {
                                childs.put(parent, new ItemBean(parent, canCheck));
                            }
                        }
                        subRoots.put(subRoot, childs);
                    }
                }
                treeItems.put(root, subRoots);
            }
            request.setAttribute("treeItems", treeItems);
%>
<%@include file="../includes/jspVariables.jsp" %>
<%@include file="../includes/baseResources.jsp" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://cong.logiwareinc.com/tagutils" prefix="tagutils"%>
<%@ taglib uri="http://jakarta.apache.org/taglibs/unstandard-1.0" prefix="un"%>
<html>
    <head>
        <c:set var="path" value="${pageContext.request.contextPath}"></c:set>
        <c:set var="basePath" value="${pageContext.request.scheme}://${pageContext.request.serverName}:${pageContext.request.serverPort}${path}/"></c:set>
        <base href="${basePath}"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="${path}/css/jquery-treeview/jquery.treeview.css" type="text/css" rel="stylesheet"/>
        <script src="${path}/js/jquery/jquery-1.3.1.js" type="text/javascript"></script>
        <script src="${path}/js/jquery.treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript">
            $(document).ready(function(){
                $("#root").treeview({
                    collapsed: false
                });
            });
        </script>
    </head>
    <body class="whitebackgrnd">
    <html:form  action="/newRole" method="post" type="com.gp.cong.logisoft.struts.form.NewRoleForm" name="newRoleForm" scope="request">
            <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
        <div>
            Role Name : ${role.roleDesc}
        </div>
        <div>           
            <input type="button" class="buttonStyleNew" value="Save" onclick="save()">
            <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()">
            <input type="button" class="buttonStyleNew" value="Notes" onclick="return GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.ADMIN_ROLE_MANAGEMENT}',300,700);">
       
        
        </div>
        <ul id="root" class="filetree treeview-gray">
            <li>Logiware
                <ul>
                    <c:forEach items="${treeItems}" var="root">
                        <li>${root.key.itemDesc}
                            <ul>
                                <c:forEach items="${root.value}" var="subRoot">
                                    <li class="closed">${subRoot.key.itemDesc}
                                        <ul>
                                            <c:forEach items="${subRoot.value}" var="child">
                                                <li>
                                                    <c:choose>
                                                        <c:when test="${tagutils:instanceOf(child.value,'com.logiware.bean.ItemBean')}">
                                                            <c:choose>
                                                                <c:when test="${child.value.checked=='on'}">
                                                                    <input type="checkbox" name="checked" checked value="${child.value.checked}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="checked" value="${child.value.checked}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            ${child.value.itemDesc}
                                                            <c:choose>
                                                                <c:when test="${child.value.modify=='on'}">
                                                                    <input type="checkbox" name="modify" checked value="${child.value.modify}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="modify" value="${child.value.checked}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            Modify
                                                            <c:choose>
                                                                <c:when test="${child.value.delete=='on'}">
                                                                    <input type="checkbox" name="delete" checked value="${child.value.delete}">
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <input type="checkbox" name="delete" value="${child.value.delete}">
                                                                </c:otherwise>
                                                            </c:choose>
                                                            Delete
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${child.key.itemDesc}
                                                            <ul>
                                                                <c:forEach items="${child.value}" var="subChild">
                                                                    <li>
                                                                        <c:choose>
                                                                            <c:when test="${subChild.checked=='on'}">
                                                                                <input type="checkbox" name="checked" checked value="${subChild.checked}">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <input type="checkbox" name="checked" value="${subChild.checked}">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        ${subChild.itemDesc}
                                                                        <c:choose>
                                                                            <c:when test="${subChild.modify=='on'}">
                                                                                <input type="checkbox" name="modify" checked value="${subChild.modify}">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <input type="checkbox" name="modify" value="${subChild.checked}">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        Modify
                                                                        <c:choose>
                                                                            <c:when test="${subChild.delete=='on'}">
                                                                                <input type="checkbox" name="delete" checked value="${subChild.delete}">
                                                                            </c:when>
                                                                            <c:otherwise>
                                                                                <input type="checkbox" name="delete" value="${subChild.delete}">
                                                                            </c:otherwise>
                                                                        </c:choose>
                                                                        Delete
                                                                    </li>
                                                                </c:forEach>
                                                            </ul>
                                                        </c:otherwise>
                                                    </c:choose>
                                                </li>
                                            </c:forEach>
                                        </ul>
                                    </li>
                                </c:forEach>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>
            </li>
        </ul>
    </html:form>
</body>
</html>
