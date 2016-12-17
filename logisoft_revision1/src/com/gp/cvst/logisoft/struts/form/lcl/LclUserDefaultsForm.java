/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.domain.lcl.LclUserDefaults;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUserDefaultsDAO;
import java.util.List;

/**
 *
 * @author HOME
 */
public class LclUserDefaultsForm extends LogiwareActionForm {

    private LclUserDefaults lclUserDefaults;
    private String portOfOrigin;
    private String portOfLoading;
    private String portOfDestination;
    private String finalDestination;
    private String currentLocation;
    private Integer portOfOriginId;
    private Integer portOfLoadingId;
    private Integer portOfDestinationId;
    private Integer finalDestinationId;
    private Integer currentLocationId;
    private String methodName;
    private String applyDefaultName;
    private String lcldefaultName;
    private Integer userId;
    private String selectedMenu;
    private Integer applyDefaultId;


    public LclUserDefaults getLclUserDefaults() {
        if (lclUserDefaults == null) {
            lclUserDefaults = new LclUserDefaults();
        }
        return lclUserDefaults;
    }
    
    public void setLclUserDefaults(LclUserDefaults lclUserDefaults) {
        this.lclUserDefaults = lclUserDefaults;
    }

 //**************************************************************************
    public String getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(String currentLocation) {
        this.currentLocation = currentLocation;
    }

    public String getFinalDestination() {
        return finalDestination;
    }

    public void setFinalDestination(String finalDestination) {
        this.finalDestination = finalDestination;
    }

    public String getPortOfDestination() {
        return portOfDestination;
    }

    public void setPortOfDestination(String portOfDestination) {
        this.portOfDestination = portOfDestination;
    }

    public String getPortOfLoading() {
        return portOfLoading;
    }

    public void setPortOfLoading(String portOfLoading) {
        this.portOfLoading = portOfLoading;
    }

    public String getPortOfOrigin() {
        return portOfOrigin;
    }

    public void setPortOfOrigin(String portOfOrigin) {
        this.portOfOrigin = portOfOrigin;
    }

//**********************************************************************************
    public Integer getCurrentLocationId() {
        return currentLocationId;
    }

    public void setCurrentLocationId(Integer currentLocationId) throws Exception {
        this.currentLocationId = currentLocationId;

    }

    public Integer getFinalDestinationId() {
        return finalDestinationId;
    }

    public void setFinalDestinationId(Integer finalDestinationId) throws Exception {
        this.finalDestinationId = finalDestinationId;

    }

    public Integer getPortOfDestinationId() {
        return portOfDestinationId;
    }

    public void setPortOfDestinationId(Integer portOfDestinationId) throws Exception {
        this.portOfDestinationId = portOfDestinationId;

    }

    public Integer getPortOfLoadingId() {
        return portOfLoadingId;
    }

    public void setPortOfLoadingId(Integer portOfLoadingId) throws Exception {
        this.portOfLoadingId = portOfLoadingId;

    }

    public Integer getPortOfOriginId() {
        return portOfOriginId;
    }

    public void setPortOfOriginId(Integer portOfOriginId) throws Exception {
        this.portOfOriginId = portOfOriginId;

    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

     public String getLcldefaultName() {
        return lcldefaultName;
    }

    public void setLcldefaultName(String lcldefaultName) {
        this.lcldefaultName = lcldefaultName;
    }
     
    public String getApplyDefaultName() {
        return applyDefaultName;
    }

    public void setApplyDefaultName(String applyDefaultName) {
        this.applyDefaultName = applyDefaultName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(String selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public Integer getApplyDefaultId() {
        return applyDefaultId;
    }

    public void setApplyDefaultId(Integer applyDefaultId) {
        this.applyDefaultId = applyDefaultId;
    }
    
}
