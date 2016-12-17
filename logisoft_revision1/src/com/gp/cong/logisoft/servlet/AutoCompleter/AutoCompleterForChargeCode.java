package com.gp.cong.logisoft.servlet.AutoCompleter;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.bc.accounting.GLMappingConstant;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cvst.logisoft.domain.GlMapping;
import com.gp.cvst.logisoft.hibernate.dao.GlMappingDAO;

public class AutoCompleterForChargeCode {

    public void setChargeCodes(HttpServletRequest request, HttpServletResponse response)throws Exception{
            PrintWriter out = response.getWriter();
            String textFieldId = request.getParameter("textFieldId");
            if (textFieldId == null) {
                return;
            }
            String from = request.getParameter("from");
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("<ul>");
            if (CommonUtils.isEqual(from, "Accruals")) {
                List<GlMapping> glMappings = new GlMappingDAO().getGLMappingForAccruals("%" + request.getParameter(textFieldId) + "%");
                if (CommonUtils.isNotEmpty(glMappings)) {
                    for (GlMapping glMapping : glMappings) {
                        String blueScreenChargeCode = null != glMapping.getBlueScreenChargeCode() ? glMapping.getBlueScreenChargeCode() : "";
                        String chargeCode = glMapping.getChargeCode();
                        String chargeDescriptions = null != glMapping.getChargeDescriptions() ? glMapping.getChargeDescriptions() : "";
                        String shipmentType = glMapping.getShipmentType();
                        String deriveYn = glMapping.getSuffixValue();
                        if (CommonUtils.isEqualIgnoreCase(glMapping.getDeriveYn(), GLMappingConstant.FIXED)
                                || CommonUtils.isEqualIgnoreCase(glMapping.getDeriveYn(), CommonConstants.NO)) {
                            deriveYn = glMapping.getDeriveYn();
                        }else{
                            deriveYn = glMapping.getSuffixValue();
                        }
                        stringBuilder.append("<li id='").append(blueScreenChargeCode).append("@").append(chargeCode).append("@").append(shipmentType).append("@").append(deriveYn).append("'>");
                        stringBuilder.append("<b><font class='red-90'>").append(blueScreenChargeCode).append("</font>    <font class='green'>").append(chargeCode).append("</font>    ");
                        stringBuilder.append("<font class='blue-70'>").append(chargeDescriptions).append("</font>    <font class='red'>").append(shipmentType).append("</font></b>");
                        stringBuilder.append("</li>");
                    }
                }
            } else if (CommonUtils.isEqual(from, "glMapping")) {
                String code = request.getParameter(textFieldId);
                List<GlMapping> glMappings = new GlMappingDAO().getChargeCodeForAutocomplete(code);
                if (CommonUtils.isNotEmpty(glMappings)) {
                    for (GlMapping glMapping : glMappings) {
                        stringBuilder.append("<li id='").append(glMapping.getChargeCode()).append("'>");
                        stringBuilder.append("<b><font class='red-90'>");
                        stringBuilder.append(glMapping.getChargeCode()).append("</font>");
                        if (CommonUtils.isNotEmpty(glMapping.getChargeDescriptions())) {
                            stringBuilder.append("<font class='blue-70'> <--> ");
                            stringBuilder.append(glMapping.getChargeDescriptions());
                            stringBuilder.append("</font>");
                        }
                        stringBuilder.append("</b>");
                        stringBuilder.append("</li>");
                    }
                }
            } else {
                String code = request.getParameter(textFieldId);
                String codeType = request.getParameter("codeType");
                if (null == code) {
                    code = "%";
                }
                if (null == codeType) {
                    codeType = "36";
                }
                if (null != code && !code.trim().equals("")
                        && null != codeType && !codeType.equals("")) {
                    List<GenericCode> codeList = new GenericCodeDAO().findChargeCodes(code, code, codeType);
                    if (null != codeList && !codeList.isEmpty()) {
                        for (GenericCode genericCode : codeList) {
                            stringBuilder.append("<li id='").append(genericCode.getCode().toString()).append("'>" + "<b><font class='red-90'>").append(genericCode.getCode().toString()).append("</font><font class='blue-70'> <--> ").append(genericCode.getCodedesc()).append("</font></b></li>");
                        }
                    }
                }
            }
            stringBuilder.append("</ul>");
            out.println(stringBuilder.toString());
    }
}
