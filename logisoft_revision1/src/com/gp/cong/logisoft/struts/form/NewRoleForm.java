package com.gp.cong.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import com.logiware.bean.ItemBean;
import com.logiware.utils.ListUtils;
import java.util.List;
import org.apache.struts.action.ActionForm;

public class NewRoleForm extends ActionForm {

    private static final long serialVersionUID = -3868893224517697530L;
    private String roleName;
    private String buttonValue;
    private List<ItemBean> roleItemBeans;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getButtonValue() {
        return buttonValue;
    }

    public void setButtonValue(String buttonValue) {
        this.buttonValue = buttonValue;
    }
    public List<ItemBean> getRoleItemBeans() throws Exception {
        if (CommonUtils.isEmpty(roleItemBeans)) {
            this.setRoleItemBeans(ListUtils.lazyList(ItemBean.class));
        }
        return roleItemBeans;
    }

    public void setRoleItemBeans(List<ItemBean> roleItemBeans) {
        this.roleItemBeans = roleItemBeans;
    }
}
