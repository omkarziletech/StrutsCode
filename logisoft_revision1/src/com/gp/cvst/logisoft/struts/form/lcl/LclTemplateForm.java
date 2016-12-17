/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSearchTemplateDAO;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.struts.action.ActionMapping;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author lakshh
 */
public class LclTemplateForm extends LogiwareActionForm {

    private LclSearchTemplate lclSearchTemplate;
    private List<LclSearchTemplate> templateList;
    private String templateName;
    private String templateId;
    private String moduleName;
    private String orderedData;

    public LclTemplateForm() {
        if (lclSearchTemplate == null) {
            lclSearchTemplate = new LclSearchTemplate();
        }
    }

    public List<LclSearchTemplate> getTemplateList() throws Exception {
        if (templateList == null) {
            templateList = new LclSearchTemplateDAO().getAllTemplate();
        }
        return templateList;
    }

    public void setTemplateList(List<LclSearchTemplate> templateList) {
        this.templateList = templateList;
    }

    public LclSearchTemplate getLclSearchTemplate() {
        return lclSearchTemplate;
    }

    public void setLclSearchTemplate(LclSearchTemplate lclSearchTemplate) {
        this.lclSearchTemplate = lclSearchTemplate;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        lclSearchTemplate.setTemplateName(templateName.toUpperCase());
    }

    public boolean getAesBy() {
        return lclSearchTemplate.getAesBy();
    }

    public void setAesBy(boolean aesBy) {
        lclSearchTemplate.setAesBy(aesBy);
    }

    public boolean getEdi() {
        return lclSearchTemplate.getEdi();
    }

    public void setEdi(boolean edi) {
        lclSearchTemplate.setEdi(edi);
    }

    public boolean getQu() {
        return lclSearchTemplate.getQu();
    }

    public void setQu(boolean qu) {
        lclSearchTemplate.setQu(qu);
    }

    public boolean getBk() {
        return lclSearchTemplate.getBk();
    }

    public void setBk(boolean bk) {
        lclSearchTemplate.setBk(bk);
    }

    public boolean getBl() {
        return lclSearchTemplate.getBl();
    }

    public void setBl(boolean bl) {
        lclSearchTemplate.setBl(bl);
    }

    public boolean getHz() {
        return lclSearchTemplate.getHz();
    }

    public void setHz(boolean hz) {
        lclSearchTemplate.setHz(hz);
    }

    public boolean getFileNo() {
        return lclSearchTemplate.getFileNo();
    }

    public void setFileNo(boolean fileNo) {
        lclSearchTemplate.setFileNo(fileNo);
    }

    public boolean getTr() {
        return lclSearchTemplate.getTr();
    }

    public void setTr(boolean tr) {
        lclSearchTemplate.setTr(tr);
    }
    
    public boolean getPn() {
        return lclSearchTemplate.getPn();
    }

    public void setPn(boolean pn) {
        lclSearchTemplate.setPn(pn);
    }

    public boolean getStatus() {
        return lclSearchTemplate.getStatus();
    }

    public void setStatus(boolean status) {
        lclSearchTemplate.setStatus(status);
    }

    public boolean getDoc() {
        return lclSearchTemplate.getDoc();
    }

    public void setDoc(boolean doc) {
        lclSearchTemplate.setDoc(doc);
    }

    public boolean getDateReceived() {
        return lclSearchTemplate.getDateReceived();
    }

    public void setDateReceived(boolean dateReceived) {
        lclSearchTemplate.setDateReceived(dateReceived);
    }

    public boolean getPcs() {
        return lclSearchTemplate.getPcs();
    }

    public void setPcs(boolean pcs) {
        lclSearchTemplate.setPcs(pcs);
    }

    public boolean getCube() {
        return lclSearchTemplate.getCube();
    }

    public void setCube(boolean cube) {
        lclSearchTemplate.setCube(cube);
    }

    public boolean getWeight() {
        return lclSearchTemplate.getWeight();
    }

    public void setWeight(boolean weight) {
        lclSearchTemplate.setWeight(weight);
    }

    public boolean getOrigin() {
        return lclSearchTemplate.getOrigin();
    }

    public void setOrigin(boolean origin) {
        lclSearchTemplate.setOrigin(origin);
    }

    public boolean getPol() {
        return lclSearchTemplate.getPol();
    }

    public void setPol(boolean pol) {
        lclSearchTemplate.setPol(pol);
    }

    public boolean getPod() {
        return lclSearchTemplate.getPod();
    }

    public void setPod(boolean pod) {
        lclSearchTemplate.setPod(pod);
    }

    public boolean getDestination() {
        return lclSearchTemplate.getDestination();
    }

    public void setDestination(boolean destination) {
        lclSearchTemplate.setDestination(destination);
    }

    public boolean getShipper() {
        return lclSearchTemplate.getShipper();
    }

    public void setShipper(boolean shipper) {
        lclSearchTemplate.setShipper(shipper);
    }

    public boolean getFwd() {
        return lclSearchTemplate.getFwd();
    }

    public void setFwd(boolean fwd) {
        lclSearchTemplate.setFwd(fwd);
    }

    public boolean getConsignee() {
        return lclSearchTemplate.getConsignee();
    }

    public void setConsignee(boolean consignee) {
        lclSearchTemplate.setConsignee(consignee);
    }

    public boolean getBillTm() {
        return lclSearchTemplate.getBillTm();
    }

    public void setBillTm(boolean billTm) {
        lclSearchTemplate.setBillTm(billTm);
    }

    public boolean getQuoteBy() {
        return lclSearchTemplate.getQuoteBy();
    }

    public void setQuoteBy(boolean quoteBy) {
        lclSearchTemplate.setQuoteBy(quoteBy);
    }

    public boolean getBookedBy() {
        return lclSearchTemplate.getBookedBy();
    }

    public void setBookedBy(boolean bookedBy) {
        lclSearchTemplate.setBookedBy(bookedBy);
    }

    public boolean getBookedVoy() {
        return lclSearchTemplate.getBookedVoy();
    }

    public void setBookedVoy(boolean bookedVoy) {
        lclSearchTemplate.setBookedVoy(bookedVoy);
    }

    public boolean getCons() {
        return lclSearchTemplate.getCons();
    }

    public void setCons(boolean cons) {
        lclSearchTemplate.setCons(cons);
    }

    public boolean getBookedSaildate() {
        return lclSearchTemplate.getBookedSaildate();
    }

    public void setBookedSaildate(boolean bookedSaildate) {
        lclSearchTemplate.setBookedSaildate(bookedSaildate);
    }

    public boolean getRelayOverride() {
        return lclSearchTemplate.getRelayOverride();
    }

    public void setRelayOverride(boolean relayOverride) {
        lclSearchTemplate.setRelayOverride(relayOverride);
    }

    public boolean getHotCodes() {
        return lclSearchTemplate.getHotCodes();
    }

    public void setHotCodes(boolean hotCodes) {
        lclSearchTemplate.setHotCodes(hotCodes);
    }

    public boolean getLoadLrd() {
        return lclSearchTemplate.getLoadLrd();
    }

    public void setLoadLrd(boolean loadLrd) {
        lclSearchTemplate.setLoadLrd(loadLrd);
    }

    public boolean getOriginLrd() {
        return lclSearchTemplate.getOriginLrd();
    }

    public void setOriginLrd(boolean originLrd) {
        lclSearchTemplate.setOriginLrd(originLrd);
    }

    public boolean getCurrentLocation() {
        return lclSearchTemplate.getCurrentLocation();
    }

    public void setCurrentLocation(boolean currentLocation) {
        lclSearchTemplate.setCurrentLocation(currentLocation);
    }

    public boolean getDisp() {
        return lclSearchTemplate.getDisp();
    }

    public void setDisp(boolean disp) {
        lclSearchTemplate.setDisp(disp);
    }
    
    public boolean getLineLocation() {
        return lclSearchTemplate.getLineLocation();
    }

    public void setLineLocation(boolean lineLocation) {
        lclSearchTemplate.setLineLocation(lineLocation);
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getOrderedData() {
        return orderedData;
    }

    public void setOrderedData(String orderedData) {
        this.orderedData = orderedData;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        String id = request.getParameter("id");
        if (CommonUtils.isNotEmpty(id)) {
            try {
                lclSearchTemplate = new LclSearchTemplateDAO().findById(Integer.parseInt(id));
            } catch (Exception ex) {
                Logger.getLogger(LclTemplateForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
