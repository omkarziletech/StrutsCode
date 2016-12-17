package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.cvst.logisoft.domain.FiscalPeriod;
import com.gp.cvst.logisoft.hibernate.dao.FiscalPeriodDAO;

public class AutoCompleterForFiscalPeriod {

    public void setFiscalPeriods(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String period = request.getParameter(textFieldId);
            String from = request.getParameter("from");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isNotEmpty(period)) {
                if (CommonUtils.isEqual(from, "JE")) {
                    List<FiscalPeriod> fiscalPeriods = new FiscalPeriodDAO().getAllPeriodsForOpenYear("%" + period + "%");
                    for (FiscalPeriod fiscalPeriod : fiscalPeriods) {
                        stringBuilder.append("<li id='").append(fiscalPeriod.getPeriodDis()).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(fiscalPeriod.getPeriodDis()).append("</font><font class='red-90'> <-->");
                        stringBuilder.append(fiscalPeriod.getStatus()).append("</font></b></li>");
                    }
                } else if (CommonUtils.isEqualIgnoreCase(from, "ApReports")) {
                    List<FiscalPeriod> fiscalPeriods = new FiscalPeriodDAO().findByPeriodDis("%" + period + "%");
                    for (FiscalPeriod fiscalPeriod : fiscalPeriods) {
                        stringBuilder.append("<li id='").append(fiscalPeriod.getId()).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(fiscalPeriod.getPeriodDis()).append("</font></b></li>");
                    }
                } else {
                    List<FiscalPeriod> fiscalPeriods = new FiscalPeriodDAO().findByPeriodDis("%" + period + "%");
                    for (FiscalPeriod fiscalPeriod : fiscalPeriods) {
                        stringBuilder.append("<li id='").append(fiscalPeriod.getId()).append("'>");
                        stringBuilder.append("<b><font class='blue-70'>").append(fiscalPeriod.getPeriodDis()).append("</font><font class='red-90'> <--> ");
                        stringBuilder.append(fiscalPeriod.getStatus()).append("</font></b></li>");
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
