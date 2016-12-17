package com.gp.cong.logisoft.bc.ratemanagement;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.beans.GenericCodeBean;
import java.util.List;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.CodetypeDAO;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import java.util.ArrayList;
import java.util.Iterator;
import javax.servlet.http.HttpServletRequest;

public class GenericCodeBC {

    GenericCodeDAO genericCodeDAO = new GenericCodeDAO();

    GenericCode genericCode = null;

    public GenericCode getCommodityCode(String comCode) throws Exception {
        List commodityCodeList = genericCodeDAO.findForGenericCode(comCode);
        if (commodityCodeList != null && commodityCodeList.size() > 0) {
            genericCode = (GenericCode) commodityCodeList.get(0);
        }
        return genericCode;
    }

    public GenericCode getCommodityCodeForCode(String comCode) throws Exception {
        List commodityCodeList = genericCodeDAO.findForAirRatesDup(comCode,
                null);
        if (commodityCodeList != null && commodityCodeList.size() > 0) {
            genericCode = (GenericCode) commodityCodeList.get(0);
        }
        return genericCode;
    }

    public GenericCode getSalesCodeDesc(String salesCode) throws Exception {
        GenericCode salecode = genericCodeDAO.findById(Integer
                .parseInt(salesCode));

        return salecode;
    }

    /**
     * @param codeValue
     * @param comCodedesc
     * @return
     */
    public GenericCode getCommodityCodeDescription(String codeValue,
            String comCodedesc) throws Exception {
        List commodityCodeDescriptionList = genericCodeDAO.findForAirRates("",
                comCodedesc);
        if (commodityCodeDescriptionList != null
                && commodityCodeDescriptionList.size() > 0) {
            genericCode = (GenericCode) commodityCodeDescriptionList.get(0);
        }
        return genericCode;
    }

    public GenericCode getCommodityForFclBl(String comCode, String comCodedesc) throws Exception {
        List commodityCodeDescriptionList = genericCodeDAO
                .findForGenericAction(14, comCode, comCodedesc);
        if (commodityCodeDescriptionList != null
                && commodityCodeDescriptionList.size() > 0) {
            genericCode = (GenericCode) commodityCodeDescriptionList.get(0);
        }

        return genericCode;
    }

    public GenericCode getCommodityDetails(String codeType, String comCode,
            String comCodedesc) throws Exception {
        int codeTypeId = 0;
        String commodityCode = "";
        String commodityCodeDesc = "";
        if (codeType != null && !codeType.equalsIgnoreCase("")) {
            codeTypeId = Integer.parseInt(codeType);
        }
        if (comCode != null && !comCode.equalsIgnoreCase("")) {
            commodityCode = comCode;
        }
        if (comCodedesc != null && !comCodedesc.equalsIgnoreCase("")) {
            commodityCodeDesc = comCodedesc;
        }
        List commodityCodeDescriptionList = genericCodeDAO
                .findForGenericAction(codeTypeId, commodityCode,
                        commodityCodeDesc);
        if (commodityCodeDescriptionList != null
                && commodityCodeDescriptionList.size() > 0) {
            genericCode = (GenericCode) commodityCodeDescriptionList.get(0);
        }
        return genericCode;
    }

    public GenericCode getChargeCodeDescription(String comCode, String codeType) throws Exception {
        List commodityCodeList = genericCodeDAO.findGenericCode(codeType, comCode);
        if (commodityCodeList != null && commodityCodeList.size() > 0) {
            genericCode = (GenericCode) commodityCodeList.get(0);
        }
        return genericCode;
    }

    public GenericCode getChargeCode(String comCodeDesc, String codeType) throws Exception {
        List commodityCodeList = genericCodeDAO.findForChargeCodesForAirRates("", comCodeDesc, codeType);
        if (commodityCodeList != null && commodityCodeList.size() > 0) {
            genericCode = (GenericCode) commodityCodeList.get(0);
        }
        return genericCode;
    }

    public List<GenericCode> getAllEventCode() throws Exception {
        return genericCodeDAO.getAllEventCode();
    }

    public List<GenericCode> getAllLPropertiesCode() throws Exception {
        return genericCodeDAO.getAllLPropertiesCode();
    }

    public List<GenericCode> getPropertiesCode(String field1) throws Exception {
        return genericCodeDAO.getProperties(field1);
    }

    public List getDetails(List genericCodeList) {
        List list = new ArrayList();
        GenericCodeBean genericCodeBean;
        if (null != genericCodeList && !genericCodeList.isEmpty()) {
            Iterator iterator = genericCodeList.iterator();
            while (iterator.hasNext()) {
                genericCodeBean = new GenericCodeBean();
                Object[] row = (Object[]) iterator.next();
                genericCodeBean.setId(String.valueOf(row[0]));
                genericCodeBean.setCode((String) row[1]);
                genericCodeBean.setCodedesc((String) row[2]);
                genericCodeBean.setDesc((String) row[3]);
                list.add(genericCodeBean);
            }
        }
        return list;
    }

    public void getGenericCodes(String codeType, String code, String description, String codeFieldId, String descFieldId, HttpServletRequest request) throws Exception {
        request.setAttribute("codeType", codeType);
        request.setAttribute("code", code);
        request.setAttribute("description", description);
        request.setAttribute("codeFieldId", codeFieldId);
        request.setAttribute("descFieldId", descFieldId);
        if (CommonUtils.isAtLeastOneNotEmpty(code, description)) {
            Integer codeTypeId = new CodetypeDAO().getCodeTypeId(codeType);
            List<GenericCode> genericCodes = genericCodeDAO.getGenericCodes(codeTypeId, code, description);
            request.setAttribute("genericCodes", genericCodes);
        }
    }
}
