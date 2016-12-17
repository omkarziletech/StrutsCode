/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.domain.lcl;

import com.gp.cong.hibernate.Domain;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

/**
 *
 * @author lakshh
 */
@Entity
@Table(name = "lcl_search_template")
@DynamicUpdate(true)
@DynamicInsert(true)
public class LclSearchTemplate extends Domain {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "template_name")
    private String templateName;
    @Basic(optional = false)
    @Column(name = "qu")
    private boolean qu;
    @Basic(optional = false)
    @Column(name = "bk")
    private boolean bk;
    @Basic(optional = false)
    @Column(name = "bl")
    private boolean bl;
    @Basic(optional = false)
    @Column(name = "hz")
    private boolean hz;
    @Basic(optional = false)
    @Column(name = "edi")
    private boolean edi;
    @Basic(optional = false)
    @Column(name = "fileFrom")
    private boolean fileFrom;
    @Basic(optional = false)
    @Column(name = "fileNo")
    private boolean fileNo;
    @Basic(optional = false)
    @Column(name = "tr")
    private boolean tr;
    @Basic(optional = false)
    @Column(name = "pn")
    private boolean pn;
    @Basic(optional = false)
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @Column(name = "doc")
    private boolean doc;
    @Basic(optional = false)
    @Column(name = "disp")
    private boolean disp;
    @Basic(optional = false)
    @Column(name = "cloc")
    private boolean cloc;
    @Basic(optional = false)
    @Column(name = "date_received")
    private boolean dateReceived;
    @Basic(optional = false)
    @Column(name = "pcs")
    private boolean pcs;
    @Basic(optional = false)
    @Column(name = "cube")
    private boolean cube;
    @Basic(optional = false)
    @Column(name = "weight")
    private boolean weight;
    @Basic(optional = false)
    @Column(name = "origin")
    private boolean origin;
    @Basic(optional = false)
    @Column(name = "pol")
    private boolean pol;
    @Basic(optional = false)
    @Column(name = "pod")
    private boolean pod;
    @Basic(optional = false)
    @Column(name = "destination")
    private boolean destination;
    @Basic(optional = false)
    @Column(name = "fd")
    private boolean fd;
    @Basic(optional = false)
    @Column(name = "bkvoy")
    private boolean bkvoy;
    @Basic(optional = false)
    @Column(name = "shipper")
    private boolean shipper;
    @Basic(optional = false)
    @Column(name = "fwd")
    private boolean fwd;
    @Basic(optional = false)
    @Column(name = "consignee")
    private boolean consignee;
    @Basic(optional = false)
    @Column(name = "bill_tm")
    private boolean billTm;
    @Basic(optional = false)
    @Column(name = "aes_by")
    private boolean aesBy;
    @Basic(optional = false)
    @Column(name = "quote_by")
    private boolean quoteBy;
    @Basic(optional = false)
    @Column(name = "booked_by")
    private boolean bookedBy;
    @Basic(optional = false)
    @Column(name = "booked_voy")
    private boolean bookedVoy;
    @Basic(optional = false)
    @Column(name = "cons")
    private boolean cons;
    @Basic(optional = false)
    @Column(name = "booked_saildate")
    private boolean bookedSaildate;
    @Basic(optional = false)
    @Column(name = "relay_override")
    private boolean relayOverride;
    @Basic(optional = false)
    @Column(name = "hot_codes")
    private boolean hotCodes;
    @Basic(optional = false)
    @Column(name = "load_lrd")
    private boolean loadLrd;
    @Basic(optional = false)
    @Column(name = "origin_lrd")
    private boolean originLrd;
    @Basic(optional = false)
    @Column(name = "current_Location")
    private boolean currentLocation;
    @Basic(optional = false)
    @Column(name = "lineLocation")
    private boolean lineLocation;
    @Column(name = "loading_remarks")
    private boolean loadingRemarks;
    public LclSearchTemplate() {
    }

    public LclSearchTemplate(Integer id) {
        this.id = id;
    }

    public LclSearchTemplate(Integer id, String templateName, boolean qu, boolean bk, boolean bl, boolean hz, boolean fileNo, boolean tr, boolean pn, boolean status, 
                            boolean doc, boolean dateReceived, boolean pcs, boolean cube, boolean weight, boolean origin, boolean pol, boolean pod, 
                                boolean destination, boolean shipper, boolean fwd, boolean consignee, boolean billTm, boolean aesBy, 
                                boolean quoteBy, boolean bookedBy, boolean bookedVoy, boolean cons, boolean bookedSaildate, boolean hotCodes, boolean loadLrd,
                                boolean fileFrom,boolean disp,boolean cloc,boolean fd,boolean bkvoy,boolean loadingRemarks) {
        this.id = id;
        this.templateName = templateName;
        this.qu = qu;
        this.bk = bk;
        this.bl = bl;
        this.hz = hz;
        this.fileNo = fileNo;
        this.tr = tr;
        this.pn = pn;
        this.status = status;
        this.doc = doc;
        this.dateReceived = dateReceived;
        this.pcs = pcs;
        this.cube = cube;
        this.weight = weight;
        this.origin = origin;
        this.pol = pol;
        this.pod = pod;
        this.destination = destination;
        this.shipper = shipper;
        this.fwd = fwd;
        this.consignee = consignee;
        this.billTm = billTm;
        this.aesBy = aesBy;
        this.quoteBy = quoteBy;
        this.bookedBy = bookedBy;
        this.bookedVoy = bookedVoy;
        this.cons = cons;
        this.bookedSaildate = bookedSaildate;
        this.hotCodes = hotCodes;
        this.loadLrd = loadLrd;
        this.fileFrom=fileFrom;
        this.disp=disp;
        this.cloc=cloc;
        this.fd=fd;
        this.bkvoy=bkvoy;
        this.loadingRemarks = loadingRemarks;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public boolean getQu() {
        return qu;
    }

    public void setQu(boolean qu) {
        this.qu = qu;
    }

    public boolean getBk() {
        return bk;
    }

    public void setBk(boolean bk) {
        this.bk = bk;
    }

    public boolean getBl() {
        return bl;
    }

    public void setBl(boolean bl) {
        this.bl = bl;
    }

    public boolean getHz() {
        return hz;
    }

    public void setHz(boolean hz) {
        this.hz = hz;
    }

    public boolean getEdi() {
        return edi;
    }

    public void setEdi(boolean edi) {
        this.edi = edi;
    }

    public boolean getFileFrom() {
        return fileFrom;
    }

    public void setFileFrom(boolean fileFrom) {
        this.fileFrom = fileFrom;
    }
    
    public boolean getFileNo() {
        return fileNo;
    }

    public void setFileNo(boolean fileNo) {
        this.fileNo = fileNo;
    }

    public boolean getPn() {
        return pn;
    }

    public void setPn(boolean pn) {
        this.pn = pn;
    }
    

    public boolean getTr() {
        return tr;
    }

    public void setTr(boolean tr) {
        this.tr = tr;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean getDoc() {
        return doc;
    }

    public boolean getDisp() {
        return disp;
    }

    public void setDisp(boolean disp) {
        this.disp = disp;
    }

    public boolean getCloc() {
        return cloc;
    }

    public void setCloc(boolean cloc) {
        this.cloc = cloc;
    }
    
    public void setDoc(boolean doc) {
        this.doc = doc;
    }

    public boolean getDateReceived() {
        return dateReceived;
    }

    public void setDateReceived(boolean dateReceived) {
        this.dateReceived = dateReceived;
    }

    public boolean getPcs() {
        return pcs;
    }

    public void setPcs(boolean pcs) {
        this.pcs = pcs;
    }

    public boolean getCube() {
        return cube;
    }

    public void setCube(boolean cube) {
        this.cube = cube;
    }

    public boolean getWeight() {
        return weight;
    }

    public void setWeight(boolean weight) {
        this.weight = weight;
    }

    public boolean getOrigin() {
        return origin;
    }

    public void setOrigin(boolean origin) {
        this.origin = origin;
    }

    public boolean getPol() {
        return pol;
    }

    public void setPol(boolean pol) {
        this.pol = pol;
    }

    public boolean getPod() {
        return pod;
    }

    public void setPod(boolean pod) {
        this.pod = pod;
    }

    public boolean getDestination() {
        return destination;
    }

    public boolean getFd() {
        return fd;
    }

    public void setFd(boolean fd) {
        this.fd = fd;
    }

    public boolean getBkvoy() {
        return bkvoy;
    }

    public void setBkvoy(boolean bkvoy) {
        this.bkvoy = bkvoy;
    }
     
    public void setDestination(boolean destination) {
        this.destination = destination;
    }

    public boolean getShipper() {
        return shipper;
    }

    public void setShipper(boolean shipper) {
        this.shipper = shipper;
    }

    public boolean getFwd() {
        return fwd;
    }

    public void setFwd(boolean fwd) {
        this.fwd = fwd;
    }

    public boolean getConsignee() {
        return consignee;
    }

    public void setConsignee(boolean consignee) {
        this.consignee = consignee;
    }

    public boolean getBillTm() {
        return billTm;
    }

    public void setBillTm(boolean billTm) {
        this.billTm = billTm;
    }

    public boolean getAesBy() {
        return aesBy;
    }

    public void setAesBy(boolean aesBy) {
        this.aesBy = aesBy;
    }

    public boolean getQuoteBy() {
        return quoteBy;
    }

    public void setQuoteBy(boolean quoteBy) {
        this.quoteBy = quoteBy;
    }

    public boolean getBookedBy() {
        return bookedBy;
    }

    public void setBookedBy(boolean bookedBy) {
        this.bookedBy = bookedBy;
    }

    public boolean getBookedVoy() {
        return bookedVoy;
    }

    public void setBookedVoy(boolean bookedVoy) {
        this.bookedVoy = bookedVoy;
    }

    public boolean getCons() {
        return cons;
    }

    public void setCons(boolean cons) {
        this.cons = cons;
    }

    public boolean getBookedSaildate() {
        return bookedSaildate;
    }

    public void setBookedSaildate(boolean bookedSaildate) {
        this.bookedSaildate = bookedSaildate;
    }

    public boolean getRelayOverride() {
        return relayOverride;
    }

    public void setRelayOverride(boolean relayOverride) {
        this.relayOverride = relayOverride;
    }

    public boolean getHotCodes() {
        return hotCodes;
    }

    public void setHotCodes(boolean hotCodes) {
        this.hotCodes = hotCodes;
    }

    public boolean getLoadLrd() {
        return loadLrd;
    }

    public void setLoadLrd(boolean loadLrd) {
        this.loadLrd = loadLrd;
    }

    public boolean getOriginLrd() {
        return originLrd;
    }

    public void setOriginLrd(boolean originLrd) {
        this.originLrd = originLrd;
    }

    public boolean getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(boolean currentLocation) {
        this.currentLocation = currentLocation;
    }

    public boolean getLineLocation() {
        return lineLocation;
    }

    public void setLineLocation(boolean lineLocation) {
        this.lineLocation = lineLocation;
    }

    public boolean isLoadingRemarks() {
        return loadingRemarks;
    }

    public void setLoadingRemarks(boolean loadingRemarks) {
        this.loadingRemarks = loadingRemarks;
    }
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LclSearchTemplate)) {
            return false;
        }
        LclSearchTemplate other = (LclSearchTemplate) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.gp.cong.logisoft.domain.lcl.LclSearchTemplate[id=" + id + "]";
    }
}
