
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>
<html>
<body>
  <h1>Formatting with the default locale</h1>
  <jsp:useBean id="now" class="java.util.Date" />
  Date: <fmt:formatDate value="${now}" dateStyle="full" />
  Number: <fmt:formatNumber value="${now.time}" />
</body>
</html>