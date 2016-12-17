/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.logiware.form.*;
import com.gp.cong.logisoft.hibernate.dao.UnLocationDAO;

/**
 *
 * @author Admin
 */
public class LclExportBlueToLogiForm extends LogiwareActionForm {

    private String destination;
    private Integer destId;
    private String load;
    private String methodName;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getLoad() {
        return load;
    }

    public void setLoad(String load) {
        this.load = load;
    }

    public Integer getDestId() {
        return destId;
    }

    public void setDestId(Integer destId) {
        this.destId = destId;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }
}
