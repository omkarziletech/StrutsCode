<%-- 
    Document   : getLclExportsVoyage
    Created on : Mar 18, 2016, 10:40:59 PM
    Author     : PALRAJ
--%>
<%@ page language="java"
         import="java.util.*,
         com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO,
         com.gp.cong.logisoft.hibernate.dao.UnLocationDAO"
         pageEncoding="ISO-8859-1"%>
<%
    String voyageNo = "";
    String originUnCode = "";
    String destinationUnCode = "";
    Integer origin5Digit = null;
    Integer destination5Digit = null;
    if (request.getParameter("origin") != null && request.getParameter("destination") != null && request.getParameter("vaoyageInternational") != null) {
        originUnCode = request.getParameter("origin").substring(request.getParameter("origin").lastIndexOf("(") + 1,
                request.getParameter("origin").lastIndexOf(")"));
        destinationUnCode = request.getParameter("destination").substring(request.getParameter("destination").lastIndexOf("(") + 1,
                request.getParameter("destination").lastIndexOf(")"));
        voyageNo = request.getParameter("vaoyageInternational");
    }
    origin5Digit = new UnLocationDAO().getUnLocIdByUnLocCode(originUnCode);
    destination5Digit = new UnLocationDAO().getUnLocIdByUnLocCode(destinationUnCode);
    Iterator cfclVoyageNo = new BookingFclDAO().getVoyageNo(origin5Digit, destination5Digit, voyageNo);
    StringBuilder sb = new StringBuilder("<UL>");
    while (cfclVoyageNo.hasNext()) {
        Object cfclVoyage[] = (Object[]) cfclVoyageNo.next();
        sb.append("<li>");
        sb.append("<font color='#093ba1'>");
        sb.append(cfclVoyage[1]);
        sb.append("</font>");
        sb.append("</li>");
    }
    sb.append("</UL>");
    out.println(sb.toString());

%>

