<%-- 
    Document   : test
    Created on : Oct 4, 2013, 1:03:30 AM
    Author     : Lakshmi Narayanan
--%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@taglib uri="http://cong.logiwareinc.com/dao" prefix="dao"%>
<c:set var="path" value="${pageContext.request.contextPath}"/>
<html>
    <head>
        <title>Test</title>
        <link type="text/css" rel="stylesheet" href="${path}/css/common.css"/>
        <script type="text/javascript" src="${path}/js/jquery/jquery.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.util.js"></script>
        <script type="text/javascript" src="${path}/js/jquery/jquery.table-filter.js"></script>
        <script type="text/javascript">
            $(document).ready(function () {
                $(".filterable").tablefilter({
                    class: "table-filter",
                    container: "#result-table"
                });
            });
        </script>
    </head>
    <body>
        <div class="scrollable-table">
            <div>
                <div>
                    <table id="result-table">
                        <thead>
                            <tr>
                                <th><div label="First Name"></div></th>
                                <th><div label="Last Name"></div><img src="${path}/images/icons/filter.png" class="filterable"/></th>
                                <th><div label="User Id"></div><img src="${path}/images/icons/filter.png" class="filterable"/></th>
                                <th><div label="Role"></div><img src="${path}/images/icons/filter.png" class="filterable"/></th>
                                <th><div label="Email"></div><img src="${path}/images/icons/filter.png" class="filterable"/></th>
                                <th><div label="Column 1"></div></th>
                                <th><div label="Column 2"></div></th>
                                <th><div label="Column 3"></div></th>
                                <th><div label="Column 4"></div></th>
                                <th><div label="Column 5"></div></th>
                                <th><div label="Column 6"></div></th>
                                <th><div label="Column 7"></div></th>
                                <th><div label="Column 8"></div></th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td>Lakshmi</td>
                                <td>Narayanan</td>
                                <td>A0001</td>
                                <td></td>
                                <td>lakshh1984@gmail.com</td>
                                <td>Column 1</td>
                                <td>Column 2</td>
                                <td>Column 3</td>
                                <td>Column 4</td>
                                <td>Column 5</td>
                                <td>Column 6</td>
                                <td>Column 7</td>
                                <td>Column 8</td>
                            </tr>
                            <tr>
                                <td>User 1</td>
                                <td>User 1</td>
                                <td>U0003</td>
                                <td>User</td>
                                <td>user1@gmail.com</td>
                                <td>Column 1</td>
                                <td>Column 2</td>
                                <td>Column 3</td>
                                <td>Column 4</td>
                                <td>Column 5</td>
                                <td>Column 6</td>
                                <td>Column 7</td>
                                <td>Column 8</td>
                            </tr>
                            <tr>
                                <td>User 2</td>
                                <td>User 2</td>
                                <td>U0004</td>
                                <td>User</td>
                                <td>user2@gmail.com</td>
                                <td>Column 1</td>
                                <td>Column 2</td>
                                <td>Column 3</td>
                                <td>Column 4</td>
                                <td>Column 5</td>
                                <td>Column 6</td>
                                <td>Column 7</td>
                                <td>Column 8</td>
                            </tr>
                            <tr>
                                <td>User 3</td>
                                <td>User 3</td>
                                <td>U0005</td>
                                <td>User</td>
                                <td>user3@gmail.com</td>
                                <td>Column 1</td>
                                <td>Column 2</td>
                                <td>Column 3</td>
                                <td>Column 4</td>
                                <td>Column 5</td>
                                <td>Column 6</td>
                                <td>Column 7</td>
                                <td>Column 8</td>
                            </tr>
                            <tr>
                                <td>Lakshmi</td>
                                <td>Narayanan</td>
                                <td>A0002</td>
                                <td>Admin</td>
                                <td>lakshh2512@gmail.com</td>
                                <td>Column 1</td>
                                <td>Column 2</td>
                                <td>Column 3</td>
                                <td>Column 4</td>
                                <td>Column 5</td>
                                <td>Column 6</td>
                                <td>Column 7</td>
                                <td>Column 8</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>
