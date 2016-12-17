package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.Ports;
import com.gp.cong.logisoft.hibernate.dao.PortsDAO;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoCompleterForPorts {

    public void setOriginTerminals(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String portName = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(portName)) {
                List result = new PortsDAO().getOriginTerminal(portName);
                if(CommonUtils.isNotEmpty(result)){
                    for(Object row : result){
                        Object[] col = (Object[])row;
                        stringBuilder.append("<li id='").append(col[0]).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(col[1]);
                        stringBuilder.append("</font><font class='red-90'> <--> ").append(col[0]).append(" </font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }

    public void setDestinationPorts(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String portName = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(portName)) {
                List result = new PortsDAO().getDestinationPorts(portName);
                if(CommonUtils.isNotEmpty(result)){
                    for(Object row : result){
                        Object[] col = (Object[])row;
                        stringBuilder.append("<li id='").append(col[0]).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(col[1]);
                        stringBuilder.append("</font><font class='red-90'> <--> ").append(col[0]).append(" </font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }

    public void setDPorts(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String portName = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(portName)) {
                List result = new PortsDAO().getDPorts(portName);
                if(CommonUtils.isNotEmpty(result)){
                    for(Object row : result){
                        Object[] col = (Object[])row;
                        stringBuilder.append("<li id='").append(col[0]).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(col[1]);
                        stringBuilder.append("</font><font class='red-90'> <--> ").append(col[0]).append(" </font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }

	public void setDestination(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String destination = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(destination)) {
                List<Ports> portsList = new PortsDAO().getDestination(destination);
				for(Ports port : portsList){
					stringBuilder.append("<li id='").append(port.getEciportcode()).append("'>");
					stringBuilder.append("<b><font class='red-90'>").append(port.getEciportcode());
					stringBuilder.append("</font><font class='blue-70'> <--> ").append(port.getPortname()).append("</font></b>");
					stringBuilder.append("</li>");
				}
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
