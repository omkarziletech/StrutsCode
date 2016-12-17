<%--
    Document   : Edit Role
    Created on : Sep 21, 2010, 2:50:00 PM
    Author     : Lakshmi Naryanan
--%>
<%@page import="com.gp.cong.logisoft.domain.Role"%>
<%@page import="com.gp.cong.logisoft.hibernate.dao.RoleItemAssociationDAO"%>
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
            Role role = (Role) session.getAttribute("role");
            ItemDAO itemDAO = new ItemDAO();
            LinkedHashMap<Item, LinkedHashMap<Item, LinkedHashMap<Item, Object>>> treeItems = new LinkedHashMap<Item, LinkedHashMap<Item, LinkedHashMap<Item, Object>>>();
            List<Item> rootItems = itemDAO.getMenu();
            for (Item root : rootItems) {
                List<Item> subRootItems = new ItemDAO().getSubMenu(root.getItemId());
                LinkedHashMap<Item, LinkedHashMap<Item, Object>> subRoots = new LinkedHashMap<Item, LinkedHashMap<Item, Object>>();
                for (Item subRoot : subRootItems) {
                    if (CommonUtils.isEmpty(subRoot.getProgramName())) {
                        List<Item> parentItems = new ItemDAO().getSubMenu(subRoot.getItemId());
                        LinkedHashMap<Item, Object> childs = new LinkedHashMap<Item, Object>();
                        for (Item parent : parentItems) {
                            if (CommonUtils.isEmpty(parent.getProgramName())) {
                                List<Item> childItems = new ItemDAO().getSubMenu(parent.getItemId());
                                List<ItemBean> childItemBeans = new ArrayList<ItemBean>();
                                for (Item childItem : childItems) {
                                    boolean canCheck = new RoleItemAssociationDAO().checkItemForRole(role.getRoleId(), childItem.getItemId());
                                    boolean canModify = new RoleItemAssociationDAO().modifyItemForRole(role.getRoleId(), childItem.getItemId());
                                    childItemBeans.add(new ItemBean(childItem, canCheck, canModify));
                                }
                                childs.put(parent, childItemBeans);
                            } else {
                                boolean canCheck = new RoleItemAssociationDAO().checkItemForRole(role.getRoleId(), parent.getItemId());
                                boolean canModify = new RoleItemAssociationDAO().modifyItemForRole(role.getRoleId(), parent.getItemId());
                                childs.put(parent, new ItemBean(parent, canCheck, canModify));
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
<%@include file="../includes/resources.jsp" %>
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
        <html:form  action="/editRole?viewOnly=${viewOnly}" method="post" type="com.gp.cong.logisoft.struts.form.EditRoleForm" name="editRoleForm" scope="request">
            <un:useConstants className="com.gp.cong.logisoft.bc.notes.NotesConstants" var="notesConstants"/>
            <font color="blue"><h4>${param.message}</h4></font>
            <div>
                <b>Role Name : <html:text property="roleName" value="${role.roleDesc}" readonly="true" styleClass="textlabelsBoldForTextBoxDisabledLook"/></b>
                <c:if test="${param.viewOnly==false}">
                    <input type="button" class="buttonStyleNew" value="Save" onclick="save()">
                    <input type="button" class="buttonStyleNew" value="Delete" onclick="deleteRole()">
                    <input type="button" class="buttonStyleNew" value="Notes" onclick="return GB_show('Notes', '${path}/notes.do?moduleId=${notesConstants.ADMIN_ROLE_MANAGEMENT}',300,700);">
                </c:if>
                <input type="button" class="buttonStyleNew" value="Go Back" onclick="goBack()">
            </div>
            <br>
            <div style="color: blue;font-weight: bolder;">
                Logiware
            </div>
            <div style="padding-left: 20px">
                <ul id="root" class="filetree treeview-red">
                    <c:set var="index" value="0"/>
                    <c:choose>
                        <c:when test="${param.viewOnly}">
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
                                                                    <c:choose>
                                                                        <c:when test="${child.value.checked=='on'}">
                                                                            <img alt="" src="${path}/img/icons/ok.gif">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img alt="" src="${path}/img/icons/remove.gif">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    ${child.value.itemDesc}
                                                                    <c:choose>
                                                                        <c:when test="${child.value.modify=='on'}">
                                                                            <img alt="" src="${path}/img/icons/ok.gif">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <img alt="" src="${path}/img/icons/remove.gif">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    Modify
                                                                    <c:set var="index" value="${index+1}"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${child.key.itemDesc}
                                                                    <ul>
                                                                        <c:forEach items="${child.value}" var="subChild">
                                                                            <li class="textLabelsBold">
                                                                                <c:choose>
                                                                                    <c:when test="${subChild.checked=='on'}">
                                                                                        <img alt="" src="${path}/img/icons/ok.gif">
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <img alt="" src="${path}/img/icons/remove.gif">
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                                ${subChild.itemDesc}
                                                                                <c:choose>
                                                                                    <c:when test="${subChild.modify=='on'}">
                                                                                        <img alt="" src="${path}/img/icons/ok.gif">
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <img alt="" src="${path}/img/icons/remove.gif">
                                                                                    </c:otherwise>
                                                                                </c:choose>
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
                        </c:when>
                        <c:otherwise>
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
                                                                    <c:choose>
                                                                        <c:when test="${child.value.checked=='on'}">
                                                                            <input type="checkbox" name="roleItemBeans[${index}].checked" checked value="${child.value.checked}">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input type="checkbox" name="roleItemBeans[${index}].checked">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    ${child.value.itemDesc}
                                                                    <c:choose>
                                                                        <c:when test="${child.value.modify=='on'}">
                                                                            <input type="checkbox" name="roleItemBeans[${index}].modify" checked value="${child.value.modify}">
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            <input type="checkbox" name="roleItemBeans[${index}].modify">
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                    Modify
                                                                    <c:set var="index" value="${index+1}"/>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    ${child.key.itemDesc}
                                                                    <ul>
                                                                        <c:forEach items="${child.value}" var="subChild">
                                                                            <li class="textLabelsBold">
                                                                                <input type="hidden" name="roleItemBeans[${index}].itemId" value="${subChild.itemId}">
                                                                                <c:choose>
                                                                                    <c:when test="${subChild.checked=='on'}">
                                                                                        <input type="checkbox" name="roleItemBeans[${index}].checked" checked value="${subChild.checked}">
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <input type="checkbox" name="roleItemBeans[${index}].checked">
                                                                                    </c:otherwise>
                                                                                </c:choose>
                                                                                ${subChild.itemDesc}
                                                                                <c:choose>
                                                                                    <c:when test="${subChild.modify=='on'}">
                                                                                        <input type="checkbox" name="roleItemBeans[${index}].modify" checked value="${subChild.modify}">
                                                                                    </c:when>
                                                                                    <c:otherwise>
                                                                                        <input type="checkbox" name="roleItemBeans[${index}].modify">
                                                                                    </c:otherwise>
                                                                                </c:choose>
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
                        </c:otherwise>
                    </c:choose>
                </ul>
            </div>
            <html:hidden property="buttonValue"/>
        </html:form>
    </body>
    <script type="text/javascript">
        function save(){
            document.editRoleForm.buttonValue.value="save";
            document.editRoleForm.submit();
        }
        function goBack(){
            document.editRoleForm.buttonValue.value="cancel";
            document.editRoleForm.submit();
        }
        function deleteRole(){
            if(confirm("Are you want to delete this role?")){
                document.editRoleForm.buttonValue.value="delete";
                document.editRoleForm.submit();
            }
        }
    </script>
</html>
