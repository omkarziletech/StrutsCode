package com.logiware.common.form;

import com.logiware.common.model.ResultModel;
import java.util.List;

/**
 *
 * @author Lakshmi Narayanan
 */
public class OnlineUserForm extends BaseForm {

    private Integer id;
    private List<ResultModel> onlineUsers;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<ResultModel> getOnlineUsers() {
        return onlineUsers;
    }

    public void setOnlineUsers(List<ResultModel> onlineUsers) {
        this.onlineUsers = onlineUsers;
    }

}
