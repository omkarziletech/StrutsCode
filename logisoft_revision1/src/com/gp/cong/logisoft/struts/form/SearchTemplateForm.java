package com.gp.cong.logisoft.struts.form;

import com.gp.cong.logisoft.domain.lcl.LclSearchTemplate;
import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;
import org.apache.struts.action.ActionForm;

public class SearchTemplateForm extends LogiwareActionForm {

    private LclSearchTemplate lclSearchTemplate;
    private String methodName;

    public SearchTemplateForm() {
        if (lclSearchTemplate == null) {
            lclSearchTemplate = new LclSearchTemplate();
        }
    }

    public LclSearchTemplate getLclSearchTemplate() {
        return lclSearchTemplate;
    }

    public void setLclSearchTemplate(LclSearchTemplate lclSearchTemplate) {
        this.lclSearchTemplate = lclSearchTemplate;
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

    public boolean getEdi() {
        return lclSearchTemplate.getEdi();
    }

    public void setEdi(boolean edi) {
        lclSearchTemplate.setEdi(edi);
    }

    public boolean getFileNo() {
        return lclSearchTemplate.getFileNo();
    }

    public void setFileNo(boolean fileNo) {
        lclSearchTemplate.setFileNo(fileNo);
    }
    public boolean getPn() {
        return lclSearchTemplate.getPn();
    }

    public void setPn(boolean pn) {
        lclSearchTemplate.setPn(pn);
    }
    public boolean getTr() {
        return lclSearchTemplate.getTr();
    }

    public void setTr(boolean tr) {
        lclSearchTemplate.setTr(tr);
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

    public boolean getAesBy() {
        return lclSearchTemplate.getAesBy();
    }

    public void setAesBy(boolean aesBy) {
        lclSearchTemplate.setAesBy(aesBy);
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

    public boolean getRelayOverride() {
        return lclSearchTemplate.getRelayOverride();
    }

    public void setRelayOverride(boolean relayOverride) {
        lclSearchTemplate.setRelayOverride(relayOverride);
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

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
