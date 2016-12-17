package com.gp.cong.logisoft.struts.action;

import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import com.gp.cong.logisoft.struts.action.lcl.LogiwareDispatchAction;
import com.gp.cong.logisoft.struts.form.SearchTemplateForm;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class SearchTemplateAction extends LogiwareDispatchAction {

    public ActionForward display(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        LclSearchTemplate lclSearchTemplate = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
        if(lclSearchTemplate==null)
        {
            lclSearchTemplate = new LclSearchTemplate();
        }
        request.setAttribute("lclSearchTemplate", lclSearchTemplate);
        return mapping.findForward("display");
    }

    public ActionForward saveTemplate(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = ((HttpServletRequest) request).getSession();
        SearchTemplateForm searchTemplateForm = (SearchTemplateForm) form;
        LclSearchTemplateDAO lclSearchTemplateDAO = new LclSearchTemplateDAO();
        LclSearchTemplate lclSearchTemplate = lclSearchTemplateDAO.getTemplateByName("LCLSEARCH");
        if (lclSearchTemplate==null) {
            lclSearchTemplate = new LclSearchTemplate();
        }
        lclSearchTemplate.setTemplateName("LCLSEARCH");
        lclSearchTemplate.setQu(searchTemplateForm.getQu());
        lclSearchTemplate.setBk(searchTemplateForm.getBk());
        lclSearchTemplate.setBl(searchTemplateForm.getBl());
        lclSearchTemplate.setHz(searchTemplateForm.getHz());
        lclSearchTemplate.setEdi(searchTemplateForm.getEdi());
        lclSearchTemplate.setFileNo(searchTemplateForm.getFileNo());
        lclSearchTemplate.setTr(searchTemplateForm.getTr());
        lclSearchTemplate.setPn(searchTemplateForm.getPn());
        lclSearchTemplate.setStatus(searchTemplateForm.getStatus());
        lclSearchTemplate.setDoc(searchTemplateForm.getDoc());
        lclSearchTemplate.setDateReceived(searchTemplateForm.getDateReceived());
        lclSearchTemplate.setPcs(searchTemplateForm.getPcs());
        lclSearchTemplate.setCube(searchTemplateForm.getCube());
        lclSearchTemplate.setWeight(searchTemplateForm.getWeight());
        lclSearchTemplate.setOrigin(searchTemplateForm.getOrigin());
        lclSearchTemplate.setPol(searchTemplateForm.getPol());
        lclSearchTemplate.setPod(searchTemplateForm.getPod());
        lclSearchTemplate.setDestination(searchTemplateForm.getDestination());
        lclSearchTemplate.setShipper(searchTemplateForm.getShipper());
        lclSearchTemplate.setFwd(searchTemplateForm.getFwd());
        lclSearchTemplate.setConsignee(searchTemplateForm.getConsignee());
        lclSearchTemplate.setBillTm(searchTemplateForm.getBillTm());
        lclSearchTemplate.setAesBy(searchTemplateForm.getAesBy());
        lclSearchTemplate.setQuoteBy(searchTemplateForm.getQuoteBy());
        lclSearchTemplate.setBookedBy(searchTemplateForm.getBookedBy());
        lclSearchTemplate.setCons(searchTemplateForm.getCons());
        lclSearchTemplate.setBookedSaildate(searchTemplateForm.getBookedSaildate());
        lclSearchTemplate.setHotCodes(searchTemplateForm.getHotCodes());
        lclSearchTemplate.setLoadLrd(searchTemplateForm.getLoadLrd());
        lclSearchTemplate.setRelayOverride(searchTemplateForm.getRelayOverride());
        lclSearchTemplate.setOriginLrd(searchTemplateForm.getOriginLrd());
        lclSearchTemplate.setCurrentLocation(searchTemplateForm.getCurrentLocation());
        lclSearchTemplateDAO.saveOrUpdate(lclSearchTemplate);
        request.setAttribute("lclSearchTemplate", lclSearchTemplate);
        return mapping.findForward("success");
    }
}
