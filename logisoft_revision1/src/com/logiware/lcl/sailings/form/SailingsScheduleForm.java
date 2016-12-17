/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.lcl.sailings.form;

import com.gp.cong.logisoft.lcl.model.LclBookingVoyageBean;
import com.gp.cvst.logisoft.struts.form.lcl.LogiwareActionForm;
import java.util.List;

/**
 *
 * @author Mei
 */
public class SailingsScheduleForm extends LogiwareActionForm {

    private String pooName;
    private String polName;
    private String podName;
    private String fdName;
    private Integer pooId;
    private Integer polId;
    private Integer podId;
    private Integer fdId;
    private String methodName;
    private List<LclBookingVoyageBean> sailingScheduleList;

    public Integer getFdId() {
        return fdId;
    }

    public void setFdId(Integer fdId) {
        this.fdId = fdId;
    }

    public String getFdName() {
        return fdName;
    }

    public void setFdName(String fdName) {
        this.fdName = fdName;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public String getPodName() {
        return podName;
    }

    public void setPodName(String podName) {
        this.podName = podName;
    }

    public Integer getPolId() {
        return polId;
    }

    public void setPolId(Integer polId) {
        this.polId = polId;
    }

    public String getPolName() {
        return polName;
    }

    public void setPolName(String polName) {
        this.polName = polName;
    }

    public Integer getPooId() {
        return pooId;
    }

    public void setPooId(Integer pooId) {
        this.pooId = pooId;
    }

    public String getPooName() {
        return pooName;
    }

    public void setPooName(String pooName) {
        this.pooName = pooName;
    }

    public List<LclBookingVoyageBean> getSailingScheduleList() {
        return sailingScheduleList;
    }

    public void setSailingScheduleList(List<LclBookingVoyageBean> sailingScheduleList) {
        this.sailingScheduleList = sailingScheduleList;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
