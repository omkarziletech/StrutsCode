<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cvst.logisoft.util.DBUtil,com.gp.cvst.logisoft.hibernate.dao.BookingFclDAO,com.gp.cvst.logisoft.domain.BookingFcl"
         pageEncoding="ISO-8859-1"%>
<%
            String accNo = "";
            String functionName = null;
            if (request.getParameter("tabName") != null) {
                functionName = request.getParameter("tabName");
            }
            if (functionName == null) {
                return;
            }
            if (functionName.equals("FCL_BILL_LADDING")
                    || functionName.equals("FCL_BL")) {
                if (request.getParameter("booking") != null
                        && request.getParameter("from") != null
                        && request.getParameter("from").equals("0")) {
                    accNo = request.getParameter("booking");
                }
                if (request.getParameter("newMasterBL") != null
                        && request.getParameter("from") != null
                        && request.getParameter("from").equals("1")) {
                    accNo = request.getParameter("newMasterBL");
                }
                }

            JSONArray accountNoArray = new JSONArray();
            List accountList = null;
            if (accNo != null && !accNo.trim().equals("")) {
                BookingFclDAO batchDAO = new BookingFclDAO();
                accountList = batchDAO.searchForBookingNo(accNo);
                Iterator iter = accountList.iterator();
                while (iter.hasNext()) {
                    BookingFcl accountDetails = (BookingFcl) iter.next();
                    if (accountDetails.getSSBookingNo() != null) {
				accountNoArray.put(accountDetails.getSSBookingNo().toString());
                    }
                }
            }

	if("false".equals(request.getParameter("isDojo"))){
                StringBuilder buffer = new StringBuilder("<ul>");
            for(int i =0; i < accountNoArray.length(); i++){
                    buffer.append("<li>");
                    buffer.append("<span class='blue-70'>");
                    buffer.append(accountNoArray.get(i));
                    buffer.append("</span>");
                    buffer.append("</li>");
                }
                buffer.append("</ul>");
                out.println(buffer.toString());
    }else{
                out.println(accountNoArray.toString());
            }
%>

