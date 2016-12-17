<%@ page language="java"
	import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO,com.gp.cong.logisoft.domain.RefTerminalTemp"%>

<%
	String portNo = "";
	String functionName = null;
	if (request.getParameter("tabName") != null) {
		functionName = request.getParameter("tabName");
	}
	if (functionName == null) {
		return;
	}

	if (functionName.equals("TERMINAL_MANAGEMENT")) {
		if (request.getParameter("terminalId") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			portNo = request.getParameter("terminalId");
		}
	} else if (functionName.equals("ADD_FUTURE_FCL_POPUP")
			|| functionName.equals("PRAC_MANAGE_RETAIL_RATES")
			|| functionName.equals("UNIVERSAL_ADD_POPUP")
			|| functionName.equals("INLAND_VOYAGE")
			|| functionName.equals("EXPORT_VOYAGE")) {
		if (request.getParameter("terminalNumber") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			portNo = request.getParameter("terminalNumber");
		}else 	if (request.getParameter("origin") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			portNo = request.getParameter("origin");
		}
	} else if (functionName.equals("FCL_BILL_LADDING")) {
		if (request.getParameter("billingTerminal") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("0")) {
			portNo = request.getParameter("billingTerminal");
		} else if (request.getParameter("originalTerminal") != null
				&& request.getParameter("from") != null
				&& request.getParameter("from").equals("1")) {
			portNo = request.getParameter("originalTerminal");
		}
	}

	JSONArray portsNoArray = new JSONArray();

	if (portNo != null && !portNo.trim().equals("")) {
		RefTerminalDAO terminalDAO = new RefTerminalDAO();

		List terminal = terminalDAO.findForManagement(portNo, null,
				null, null);
		Iterator iter = terminal.iterator();
		while (iter.hasNext()) {
			RefTerminalTemp refTerminalobj = (RefTerminalTemp) iter
					.next();
			portsNoArray.put(refTerminalobj.getTrmnum() + ":- "
					+ refTerminalobj.getTerminalLocation().trim());
		}
	}
	out.println(portsNoArray.toString());
%>
