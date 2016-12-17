package com.logiware.form;

import com.gp.cong.common.CommonUtils;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class LoginForm extends org.apache.struts.action.ActionForm {

    private String userName;
    private String password;
    private String failureMessage;
    private String warningMessage;
    private boolean loginAgain;
    private String action;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFailureMessage() {
        return failureMessage;
    }

    public void setFailureMessage(String failureMessage) {
        this.failureMessage = failureMessage;
    }

    public String getWarningMessage() {
        return warningMessage;
    }

    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    public boolean isLoginAgain() {
        return loginAgain;
    }

    public void setLoginAgain(boolean loginAgain) {
        this.loginAgain = loginAgain;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        this.userName = "";
        this.password = "";
    }

    @Override
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request) {
        ActionErrors actionErrors = new ActionErrors();
        if (CommonUtils.isEmpty(userName)) {
            reset(mapping, request);
            actionErrors.add("userName", new ActionError("error.userName.empty"));
        } else if (userName.length() < 3 || userName.length() > 100) {
            reset(mapping, request);
            actionErrors.add("userName", new ActionError("error.userName.length"));
        } else if (!userName.matches("[\\w\\.]+")) {
            reset(mapping, request);
            actionErrors.add("userName", new ActionError("error.userName.illegal"));
        } else if (CommonUtils.isEmpty(password)) {
            reset(mapping, request);
            actionErrors.add("password", new ActionError("error.password.empty"));
        } else if (password.length() < 6 || password.length() > 15) {
            reset(mapping, request);
            actionErrors.add("password", new ActionError("error.password.length"));
        } else if (!password.matches("\\w+")) {
            reset(mapping, request);
            actionErrors.add("password", new ActionError("error.password.illegal"));
        } 
        return actionErrors;
    }
}
