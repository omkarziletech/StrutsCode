package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.UnLocation;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AutoCompleterForCity {

    public void setCity(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String city = request.getParameter(textFieldId);
            StringBuilder stringBuilder = new StringBuilder();
            if (CommonUtils.isNotEmpty(city)) {
                List<UnLocation> result = new UnLocationDAO().findbyCity(city);
                if (CommonUtils.isNotEmpty(result)) {
                    stringBuilder.append("<ul>");
                    for (UnLocation unLocation : result) {
                        String cityName = unLocation.getUnLocationName().replace("'", "&#39;");
                        String unLocationCode = unLocation.getUnLocationCode();
                        String state = null!=unLocation.getStateId()?unLocation.getStateId().getCode():"";
                        String country = null!=unLocation.getCountryId()?unLocation.getCountryId().getCodedesc():"";
                        stringBuilder.append("<li id='").append(cityName).append("@").append(unLocationCode).append("@").append(state).append("@").append(country).append("' >");
                        stringBuilder.append("<div class='bold'>");
                        stringBuilder.append("<span class='blue-70'>").append(cityName).append("</span>");
                        if(CommonUtils.isNotEmpty(state)){
                            stringBuilder.append("<span class='magenta'> / ").append(state).append("</span>");
                        }
                        stringBuilder.append("<span class='green'> / ").append(country).append("</span>");
                        stringBuilder.append("<span class='red-90'> (").append(unLocationCode).append(")</span>");
                        stringBuilder.append("</div>");
                        stringBuilder.append("</li>");
                    }
                    stringBuilder.append("</ul>");
                }
            }
            out.println(stringBuilder.toString());
    }
}
