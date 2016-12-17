package com.gp.cong.logisoft.domain;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.logiware.bean.ItemBean;
import java.io.Serializable;

public class RoleItemAssociation implements Serializable {

    private static final long serialVersionUID = 1L;
    private Integer roleId;
    private Item itemId;
    private String checked;
    private String modify;
    private String deleted;
    private Integer id;

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {

        this.checked = checked;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {

        this.id = id;
    }

    public Item getItemId() {
        return itemId;
    }

    public void setItemId(Item itemId) {

        this.itemId = itemId;
    }

    public String getModify() {

        return modify;
    }

    public void setModify(String modify) {

        this.modify = modify;
    }

    public Integer getRoleId() {

        return roleId;
    }

    public void setRoleId(Integer roleId) {

        this.roleId = roleId;
    }

    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public RoleItemAssociation() {
    }

    public RoleItemAssociation(ItemBean itemBean, Integer roleId) throws Exception {
        this.roleId = roleId;
        this.itemId = new ItemDAO().findById(itemBean.getItemId());
        if (CommonUtils.isEqualIgnoreCase(itemBean.getModify(), CommonConstants.ON)) {
            this.modify = "modify";
        }
    }
}
