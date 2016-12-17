/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.lcl.model.LclConsolidateBean;
import java.util.List;

/**
 *
 * @author lakshh
 */
public class LclConsolidateForm extends LogiwareActionForm {

    private Long fileNumberId;
    private Long id;
    private List<LclConsolidateBean> consolidateFileList;
    private String consolidatedFileIds;
    private Integer podId;
    private Integer fdId;
    private Integer polId;
    private String searchType;
    private String sortByValue;
    private String pickedConsolidateDr;

    public Long getFileNumberId() {
        return fileNumberId;
    }

    public void setFileNumberId(Long fileNumberId) {
        this.fileNumberId = fileNumberId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<LclConsolidateBean> getConsolidateFileList() {
        return consolidateFileList;
    }

    public void setConsolidateFileList(List<LclConsolidateBean> consolidateFileList) {
        this.consolidateFileList = consolidateFileList;
    }

    public String getConsolidatedFileIds() {
        return consolidatedFileIds;
    }

    public void setConsolidatedFileIds(String consolidatedFileIds) {
        this.consolidatedFileIds = consolidatedFileIds;
    }

    public Integer getFdId() {
        return fdId;
    }

    public void setFdId(Integer fdId) {
        this.fdId = fdId;
    }

    public Integer getPodId() {
        return podId;
    }

    public void setPodId(Integer podId) {
        this.podId = podId;
    }

    public Integer getPolId() {
        return polId;
    }

    public void setPolId(Integer polId) {
        this.polId = polId;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public String getSortByValue() {
        return sortByValue;
    }

    public void setSortByValue(String sortByValue) {
        this.sortByValue = sortByValue;
    }

    public String getPickedConsolidateDr() {
        return pickedConsolidateDr;
    }

    public void setPickedConsolidateDr(String pickedConsolidateDr) {
        this.pickedConsolidateDr = pickedConsolidateDr;
    }
}
