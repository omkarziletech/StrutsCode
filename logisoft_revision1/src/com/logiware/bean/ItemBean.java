package com.logiware.bean;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.logisoft.domain.Item;

/**
 *
 * @author Lakshmi Narayanan
 */
public class ItemBean {

    private Integer itemId;
    private String itemDesc;
    private String checked;
    private String delete;
    private String modify;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public String getDelete() {
        return delete;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getModify() {
        return modify;
    }

    public void setModify(String modify) {
        this.modify = modify;
    }

    public ItemBean() {
    }

    public ItemBean(Item item,boolean canCheck) {
        this.itemId = item.getItemId();
        this.itemDesc = item.getItemDesc();
        if(canCheck){
            this.checked = CommonConstants.ON;
        }else{
            this.checked = CommonConstants.OFF;
        }
    }
    public ItemBean(Item item,boolean canCheck,boolean canModify) {
        this.itemId = item.getItemId();
        this.itemDesc = item.getItemDesc();
        if(canCheck){
            this.checked = CommonConstants.ON;
        }else{
            this.checked = CommonConstants.OFF;
        }
        if(canModify){
            this.modify = CommonConstants.ON;
        }else{
            this.modify = CommonConstants.OFF;
        }
    }

}
