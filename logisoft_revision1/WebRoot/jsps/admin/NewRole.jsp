<%--
    Document   : New Role
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
            LinkedHashMap<Item, LinkedHashMap<Item, LinkedHashMap<Item, Object>>> treeItems = new LinkedHashMap<Item, LinkedHashMap<Item, LinkedHashMap<Item, Object>>>();
            List<Item> rootItems = itemDAO.getMenu();
            for (Item root : rootItems) {
                List<Item> subRootItems = new ItemDAO().getSubMenu(root.getItemId());
                LinkedHashMap<Item, LinkedHashMap<Item, Object>> subRoots = new LinkedHashMap<Item, LinkedHashMap<Item, Object>>();
                for (Item subRoot : subRootItems) {
                    boolean canCheck = false;
                    if (CommonUtils.isEmpty(subRoot.getProgramName())) {
                        List<Item> parentItems = new ItemDAO().getSubMenu(subRoot.getItemId());
                        LinkedHashMap<Item, Object> childs = new LinkedHashMap<Item, Object>();
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
                <b>Role Name : <html:text property="roleName" value="${role.roleDesc}" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/></b>
                <input type="button" class="buttonStyleNew" value="Save" onclick="save()">
                <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()">
                <input type="button" class="buttonStyleNew" value="Notes" onclick="return GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.ADMIN_ROLE_MANAGEMENT}',300,700);">
            </div>
            <br>
            <div style="color: blue;font-weight: bolder;">
                Logiware
            </div>
            <div style="padding-left: 20px">
                <ul id="root" class="filetree treeview-red">
                    <c:set var="index" value="0"/>
                    <c:forEach items="${treeItems}" var="root">
                        <li><b>${root.key.itemDesc}</b>
                            <ul>
                                <c:forEach items="${root.value}" var="subRoot">
                                    <li class="closed textLabelsBold">${subRoot.key.itemDesc}
                                        <ul>
                                            <c:forEach items="${subRoot.value}" var="child">
                                                <li class="textLabelsBold">
                                                    <c:choose>
                                                        <c:when test="${tagutils:instanceOf(child.value,'com.logiware.bean.ItemBean')}">
                                                            <input type="hidden" name="roleItemBeans[${index}].itemId" value="${child.value.itemId}">
                                                            <input type="checkbox" name="roleItemBeans[${index}].checked">
                                                            ${child.value.itemDesc}
                                                            <input type="checkbox" name="roleItemBeans[${index}].modify">
                                                            Modify
                                                            <c:set var="index" value="${index+1}"/>
                                                        </c:when>
                                                        <c:otherwise>
                                                            ${child.key.itemDesc}
                                                            <ul>
                                                                <c:forEach items="${child.value}" var="subChild">
                                                                    <li class="textLabelsBold">
                                                                        <input type="hidden" name="roleItemBeans[${index}].itemId" value="${subChild.itemId}">
                                                                        <input type="checkbox" name="roleItemBeans[${index}].checked">
                                                                        ${subChild.itemDesc}
                                                                        <input type="checkbox" name="roleItemBeans[${index}].modify">
                                                                        Modify
                                                                        <c:set var="index" value="${index+1}"/>
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
            </div>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
    <script type="text/javascript">
        function save(){
            document.newRoleForm.buttonValue.value="save";
            document.newRoleForm.submit();
        }
        function goBack(){
            document.newRoleForm.buttonValue.value="cancel";
            document.newRoleForm.submit();
        }
    </script>
</html>
