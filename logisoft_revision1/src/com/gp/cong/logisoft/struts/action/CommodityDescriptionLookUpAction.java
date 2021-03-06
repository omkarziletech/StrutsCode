/*
 * Generated by MyEclipse Struts
 * Template path: templates/java/JavaClass.vtl
 */
package com.gp.cong.logisoft.struts.action;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.gp.cong.logisoft.domain.GenericCode;
import com.gp.cong.logisoft.hibernate.dao.GenericCodeDAO;
import com.gp.cong.logisoft.struts.form.CommodityDescriptionLookUpForm;

/**
 * MyEclipse Struts Creation date: 03-11-2009
 *
 * XDoclet definition:
 *
 * @struts.action path="/commodityDescriptionLookUp"
 * name="commodityDescriptionLookUpForm"
 * input="/jsps/TradingPartnermaintainance/CommodityDescriptionLookUp.jsp"
 * scope="request" validate="true"
 */
public class CommodityDescriptionLookUpAction extends Action {
    /*
     * Generated Methods
     */

    /**
     * Method execute
     *
     * @param mapping
     * @param form
     * @param request
     * @param response
     * @return ActionForward
     */
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        CommodityDescriptionLookUpForm commodityDescriptionLookUpForm = (CommodityDescriptionLookUpForm) form;// TODO Auto-generated method stub

        String button = commodityDescriptionLookUpForm.getButton();
        String commodityNo = commodityDescriptionLookUpForm.getCommodityNumber();
        String commodityDesc = commodityDescriptionLookUpForm.getCommodityDescription();
        List codeList = new ArrayList();
        String codeType = null, fromPage = null;
        String field = null;
        GenericCodeDAO genericDAO = new GenericCodeDAO();
        HttpSession session = ((HttpServletRequest) request).getSession();

        if (request.getParameter("buttonValue") != null
                && (request.getParameter("buttonValue").equals("GeneralInfo")
                || request.getParameter("buttonValue").equals("GeneralImportInfo")
                || request.getParameter("buttonValue").equals("retailCommodity")
                || request.getParameter("buttonValue").equals("fclCommodity")
                || request.getParameter("buttonValue").equals("consColoadCommodity")
                || request.getParameter("buttonValue").equals("consRetailCommodity")
                || request.getParameter("buttonValue").equals("consFclCommodity")
                || request.getParameter("buttonValue").equals("SalesCode")
                || request.getParameter("buttonValue").equals("ConsSalesCode")
                || request.getParameter("buttonValue").equals("ContactConfigCode"))) {

            commodityDesc = request.getParameter("commDesc");

            if (commodityDesc != null && commodityDesc.equals("percent")) {
                commodityDesc = "%";
            }
            if (commodityDesc != null && !commodityDesc.trim().equals("")) {
                if (request.getParameter("buttonValue") != null
                        && (request.getParameter("buttonValue").equals("GeneralInfo")
                        || request.getParameter("buttonValue").equals("GeneralImportInfo")
                        || request.getParameter("buttonValue").equals("consColoadCommodity")
                        || request.getParameter("buttonValue").equals("consRetailCommodity")
                        || request.getParameter("buttonValue").equals("consFclCommodity")
                        || request.getParameter("buttonValue").equals("fclCommodity")
                        || request.getParameter("buttonValue").equals("retailCommodity"))) {
                    codeList = genericDAO.findForAirRates(commodityNo, commodityDesc);
                    //for removing Common Commodity from the list especially for GeneralInfo 
                    codeList = getCodeListForGeneralinfo(codeList);

                } else if (request.getParameter("buttonValue") != null
                        && (request.getParameter("buttonValue").equals("SalesCode")
                        || request.getParameter("buttonValue").equals("ConsSalesCode"))) {
                    codeType = "23";
                    codeList = genericDAO.findForChargeCodesForAirRates(commodityNo, commodityDesc, codeType);

                } else if (request.getParameter("buttonValue") != null
                        && request.getParameter("buttonValue").equals("ContactConfigCode")) {
                    codeType = "22";
                    field = commodityDesc;
                    codeList = genericDAO.findCodesForContactConfig(codeType, field);
                    //session set to hide the fields for next page in display table.
                    session.setAttribute("fieldValue", field);
                    //session set to hide the fields for next page in display table.
                    session.setAttribute("ContactConfiguration", "ContactConfiguration");
                    //request fetched if contacts are added from Quotes page.
                    fromPage = request.getParameter("FromPage");
                    request.setAttribute("comingFrom", fromPage);

                }
            }
            session.setAttribute("buttonValue", request.getParameter("buttonValue"));
            session.setAttribute("codeDescriptionList", codeList);
        } else if (request.getParameter("paramId") != null) {
            codeList = (List) session.getAttribute("codeDescriptionList");
            GenericCode genericCode = (GenericCode) codeList.get(Integer.parseInt(request.getParameter("paramId")));
            if (!request.getParameter("fieldParam").equals("")) {
                field = request.getParameter("fieldParam");
            }
            List descList = new ArrayList();
            descList.add(genericCode.getCode());
            String replace = genericCode.getCodedesc().replace("'", "");
            descList.add(replace);
            descList.add(field);
            request.setAttribute("DescriptionList", descList);
            //--request set if contacts added from Quotes---
            if (request.getParameter("From") != null) {
                fromPage = request.getParameter("From");
                request.setAttribute("comingFrom", fromPage);
            }
            if (request.getParameter("button").equals("GeneralInfo")) {
                request.setAttribute("buttonValue", "GeneralInfo");
            } else if (request.getParameter("button").equals("GeneralImportInfo")) {
                request.setAttribute("buttonValue", "GeneralImportInfo");
            } else if (request.getParameter("button").equals("SalesCode")) {
                request.setAttribute("buttonValue", "SalesCode");
            } else if (request.getParameter("button").equals("ConsSalesCode")) {
                request.setAttribute("buttonValue", "ConsSalesCode");
            } else if (request.getParameter("button").equals("ContactConfigCode")) {
                request.setAttribute("buttonValue", "ContactConfigCode");
            } else if (request.getParameter("button").equals("retailCommodity")) {
                request.setAttribute("buttonValue", "retailCommodity");
            } else if (request.getParameter("button").equals("fclCommodity")) {
                request.setAttribute("buttonValue", "fclCommodity");
            } else if (request.getParameter("button").equals("consColoadCommodity")) {
                request.setAttribute("buttonValue", "consColoadCommodity");
            } else if (request.getParameter("button").equals("consRetailCommodity")) {
                request.setAttribute("buttonValue", "consRetailCommodity");
            } else if (request.getParameter("button").equals("consFclCommodity")) {
                request.setAttribute("buttonValue", "consFclCommodity");
            }

            if (session.getAttribute("codeDescriptionList") != null) {
                session.removeAttribute("codeDescriptionList");
            }
            if (session.getAttribute("buttonValue") != null) {
                session.removeAttribute("buttonValue");
            }
        }
        if (button != null && button.equals("Go")) {
            if (session.getAttribute("ContactConfiguration") != null) {
                session.removeAttribute("ContactConfiguration");
            }
            if (session.getAttribute("fieldValue") != null) {
                session.removeAttribute("fieldValue");
            }
            if (request.getParameter("action") != null
                    && (request.getParameter("action").equals("GeneralInfo")
                    || request.getParameter("action").equals("GeneralImportInfo")
                    || request.getParameter("action").equals("fclCommodity")
                    || request.getParameter("action").equals("consColoadCommodity")
                    || request.getParameter("action").equals("consRetailCommodity")
                    || request.getParameter("action").equals("consFclCommodity")
                    || request.getParameter("action").equals("retailCommodity"))) {
                //for removing Common Commodity from the list especially for GeneralInfo 
                codeList = genericDAO.findForAirRates(commodityNo, commodityDesc);
                codeList = getCodeListForGeneralinfo(codeList);
            } else if (request.getParameter("action") != null
                    && (request.getParameter("action").equals("SalesCode")
                    || request.getParameter("action").equals("ConsSalesCode"))) {
                codeType = "23";
                codeList = genericDAO.findForChargeCodesForAirRates(commodityNo, commodityDesc, codeType);
            }
            session.setAttribute("codeDescriptionList", codeList);
        }
        return mapping.findForward("success");
    }

    public List getCodeListForGeneralinfo(List codeList) {
        List codeListDup = new ArrayList();
        for (Iterator iter = codeList.iterator(); iter.hasNext();) {
            GenericCode genericCode = (GenericCode) iter.next();
            if (null != genericCode.getCode() && !genericCode.getCode().equalsIgnoreCase("000000")) {
                codeListDup.add(genericCode);
            }
        }
        return codeListDup;
    }

}
