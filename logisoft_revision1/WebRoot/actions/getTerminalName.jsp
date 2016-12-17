<%@page import="com.logiware.bean.TradeRouteBean"%>
<%@ page language="java"
         import="java.util.*,org.json.JSONArray,com.gp.cong.logisoft.hibernate.dao.RefTerminalDAO,com.gp.cong.logisoft.domain.RefTerminalTemp,com.gp.cong.logisoft.hibernate.dao.UnLocationDAO,com.gp.cong.logisoft.domain.UnLocation,com.gp.cong.logisoft.domain.RefTerminal"%>

<%
        String portName = "";
        String issuingTerminal="";
        String functionName = null;
        String importFlag = "";
        if (request.getParameter("tabName") != null) {
                functionName = request.getParameter("tabName");
        }
        if (functionName == null) {
                return;
        }
        if (functionName.equals("ADD_FUTURE_FCL_POPUP")
                        || functionName.equals("UNIVERSAL_ADD_POPUP")
                        || functionName.equals("EXPORT_VOYAGE")
                        || functionName.equals("INLAND_VOYAGE")
                        || functionName.equals("QUOTE") || functionName.equals("BOOKING")) {//
                if (request.getParameter("terminalName") != null
                                && request.getParameter("from") != null
                                && request.getParameter("from").equals("0")) {
                        portName = request.getParameter("terminalName");
                } else if (request.getParameter("originName") != null
                                && request.getParameter("from") != null
                                && request.getParameter("from").equals("1")) {
                        portName = request.getParameter("originName");
                } else if (request.getParameter("issuingTerminal") != null) {
                        issuingTerminal = request.getParameter("issuingTerminal");
                }else if (request.getParameter("billingTerminal") != null) {
                        issuingTerminal = request.getParameter("billingTerminal");
                }else if (request.getParameter("issuingTerminal") != null) {
                        issuingTerminal = request.getParameter("issuingTerminal");
                }
        }

        JSONArray portsNoArray = new JSONArray();

        if (portName != null && !portName.trim().equals("")) {
                RefTerminalDAO terminalDAO = new RefTerminalDAO();

                List terminal = terminalDAO.findForManagement(null, portName,
                                null, null);
                Iterator iter = terminal.iterator();
                while (iter.hasNext()) {
                        RefTerminalTemp refTerminalobj = (RefTerminalTemp) iter.next();
                        portsNoArray.put(refTerminalobj.getTerminalLocation());
                }
        }
        if (issuingTerminal != null && !issuingTerminal.trim().equals("")) {
                RefTerminalDAO terminalDAO = new RefTerminalDAO();
                List<RefTerminal> terminals = null;
                if(request.getParameter("importFlag") != null
                                && request.getParameter("importFlag").equals("true")){
                   terminals = terminalDAO.getAllTerminalsForDisplayForImportDojo(issuingTerminal.replace("'", "''").replace("\"", ""));
                }else{
                   terminals = terminalDAO.getAllTerminalsForDisplayForDojo(issuingTerminal.replace("'", "''").replace("\"", ""));
                }
                for(RefTerminal terminal : terminals) {
                    portsNoArray.put(terminal.getTerminalLocation()+"-"+terminal.getTrmnum());
                }
        }
        if("false".equals(request.getParameter("isDojo"))){
     StringBuilder buffer = new StringBuilder("<UL>");
            for(int i =0; i < portsNoArray.length(); i++){
                buffer.append("<li>");
                buffer.append(portsNoArray.get(i));
                buffer.append("</li>");
            }
    buffer.append("</UL>");
    out.println(buffer.toString());
    }else{
        out.println(portsNoArray.toString());
    }
%>
