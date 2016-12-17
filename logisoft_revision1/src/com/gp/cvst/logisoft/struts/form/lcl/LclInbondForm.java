package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.domain.lcl.LclFileNumber;
import com.gp.cong.logisoft.domain.lcl.LclInbond;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionMapping;
import org.apache.log4j.Logger;

/**
 *
 * @author lakshh
 */
public class LclInbondForm extends LogiwareActionForm {

    private static final Logger log = Logger.getLogger(LclInbondForm.class);
    private LclInbond lclInbond;
    private Integer inbondPortId;
    private Long id;
    private String inbondPort;
    /* common in bond fields */
    private String inbondVia;
    private String valueEstimated;
    private String weightEstimated;
    private String entryClass;
    private String customReleaseValue;
    private String itClass;
    private String status;
    private Long unitId;
    private Long headerId;
    /* common in bond fields ends*/
    private String moduleName;//module Name imports r exports
    /* only for Exports*/
    private String inbondOpenClose;
    private String eciBond;
    private String fileState;
    private String printEntry7512;

    public LclInbondForm() {
        if (lclInbond == null) {
            lclInbond = new LclInbond();
        }
        if (lclInbond.getLclFileNumber() == null) {
            lclInbond.setLclFileNumber(new LclFileNumber());
        }
    }

    public Long getFileNumberId() {
        return lclInbond.getLclFileNumber().getId();
    }

    public void setFileNumberId(Long fileNumberId) {
        lclInbond.getLclFileNumber().setId(fileNumberId);
    }

    public String getFileNumber() {
        return lclInbond.getLclFileNumber().getFileNumber();
    }

    public void setFileNumber(String fileNumber) {
        lclInbond.getLclFileNumber().setFileNumber(fileNumber);
    }

    public LclInbond getLclInbond() {
        return lclInbond;
    }

    public void setLclInbond(LclInbond lclInbond) {
        this.lclInbond = lclInbond;
    }

    public String getInbondNo() {
        return lclInbond.getInbondNo().toUpperCase();
    }

    public void setInbondNo(String inbondNo) {
        lclInbond.setInbondNo(inbondNo);
    }

    public String getInbondType() {
        return lclInbond.getInbondType();
    }

    public void setInbondType(String inbondType) {
        lclInbond.setInbondType(inbondType);
    }

    public String getInbondPort() {
        if (lclInbond.getInbondPort() != null) {
            return lclInbond.getInbondPort().getUnLocationName();
        } else {
            return "";
        }
    }

    public void setInbondPort(String inbondPort) {
        this.inbondPort = inbondPort;
    }

    public String getInbondDatetime() {
        if (lclInbond.getInbondDatetime() != null) {
            return DateUtils.parseDateToString(lclInbond.getInbondDatetime());
        }
        return "";
    }

    public void setInbondDatetime(String inbondDatetime) throws Exception {
        lclInbond.setInbondDatetime(DateUtils.parseDate(inbondDatetime, "dd-MMM-yyyy"));
    }

    public Integer getInbondPortId() {
        if (lclInbond.getInbondPort() != null) {
            return lclInbond.getInbondPort().getId();
        }
        return null;
    }

    public void setInbondPortId(Integer inbondPortId) throws Exception {
        if (CommonUtils.isNotEmpty(inbondPortId)) {
            lclInbond.setInbondPort(new UnLocationDAO().findById(inbondPortId));
        }
    }

    public Long getId() {
        return lclInbond.getInbondId();
    }

    public void setId(Long id) {
        lclInbond.setInbondId(id);
    }

    public String getCustomReleaseValue() {
        return customReleaseValue;
    }

    public void setCustomReleaseValue(String customReleaseValue) {
        this.customReleaseValue = customReleaseValue;
    }

    public String getEntryClass() {
        return entryClass;
    }

    public void setEntryClass(String entryClass) {
        this.entryClass = entryClass;
    }

    public String getInbondVia() {
        return inbondVia;
    }

    public void setInbondVia(String inbondVia) {
        this.inbondVia = inbondVia;
    }

    public String getValueEstimated() {
        return valueEstimated;
    }

    public void setValueEstimated(String valueEstimated) {
        this.valueEstimated = valueEstimated;
    }

    public String getWeightEstimated() {
        return weightEstimated;
    }

    public void setWeightEstimated(String weightEstimated) {
        this.weightEstimated = weightEstimated;
    }

    public String getItClass() {
        return itClass;
    }

    public void setItClass(String itClass) {
        this.itClass = itClass;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getHeaderId() {
        return headerId;
    }

    public void setHeaderId(Long headerId) {
        this.headerId = headerId;
    }

    public Long getUnitId() {
        return unitId;
    }

    public void setUnitId(Long unitId) {
        this.unitId = unitId;
    }

    public String getInbondOpenClose() {
        return inbondOpenClose;
    }

    public void setInbondOpenClose(String inbondOpenClose) {
        if(inbondOpenClose == "Y"){
            lclInbond.setInbondOpenClose(true);
        }else {
            lclInbond.setInbondOpenClose(false);
        }
        this.inbondOpenClose = inbondOpenClose;
    }

    public String getEciBond() {
        return eciBond;
    }

    public void setEciBond(String eciBond) {
        if(eciBond == "Y"){
            lclInbond.setEciBond(true);
        }else {
            lclInbond.setEciBond(false);
        }
        this.eciBond = eciBond;
    } 

    public String getFileState() {
        return fileState;
    }

    public void setFileState(String fileState) {
        this.fileState = fileState;
    }

    public String getPrintEntry7512() {
        return printEntry7512;
    }

    public void setPrintEntry7512(String printEntry7512) {
        this.printEntry7512 = printEntry7512;
    }
    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        try {
            super.reset(mapping, request);
            this.setInbondNo("");
            this.setInbondType("");
            this.setInbondDatetime(null);
            request.setAttribute("lclInbondForm", this);
        } catch (Exception ex) {
            log.info("reset()in LclInbondForm failed on " + new Date(),ex);
        }
    }
}
