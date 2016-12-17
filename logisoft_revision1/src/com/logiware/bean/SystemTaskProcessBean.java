package com.logiware.bean;

import java.io.Serializable;

/**
 * @since Thursday March 18,2010
 * @author LakshmiNarayanan
 */
public class SystemTaskProcessBean implements  Serializable{
    private static final long serialVersionUID = 192590010818408252L;
    private String imageName;
    private String processId;
    private String cpuMemoryUsage;
    private String imageStatus;
    private String systemTaskUser;

    public String getCpuMemoryUsage() {
        return cpuMemoryUsage;
    }

    public void setCpuMemoryUsage(String cpuMemoryUsage) {
        this.cpuMemoryUsage = cpuMemoryUsage;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageStatus() {
        return imageStatus;
    }

    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    public String getProcessId() {
        return processId;
    }

    public void setProcessId(String processId) {
        this.processId = processId;
    }

    public String getSystemTaskUser() {
        return systemTaskUser;
    }

    public void setSystemTaskUser(String systemTaskUser) {
        this.systemTaskUser = systemTaskUser;
    }



   
}
