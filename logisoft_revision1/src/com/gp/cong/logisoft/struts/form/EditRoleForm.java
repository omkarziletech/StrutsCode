package com.gp.cong.logisoft.struts.form;

import com.gp.cong.common.CommonUtils;
import com.logiware.bean.ItemBean;
import com.logiware.utils.ListUtils;
import java.util.List;
import org.apache.struts.action.ActionForm;

public class EditRoleForm extends ActionForm {
    private static final long serialVersionUID = 8793322734732367566L;

    private String buttonValue;
    private String modifyTest;
    private List<ItemBean> roleItemBeans;
    public String getModifyTest() {
        return modifyTest;
    }

    public void setModifyTest(String modifyTest) {
        this.modifyTest = modifyTest;
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
