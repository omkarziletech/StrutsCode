package com.logiware.action;

import java.io.File;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.logiware.excel.ExportUsersOnlineToExcel;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.beans.LoginUser;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.UserDAO;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.logisoft.util.SystemTaskListProcess;
import com.gp.cvst.logisoft.hibernate.dao.ArBatchDAO;
import com.logiware.bean.UserObjectBean;
import com.logiware.form.OnlineUsersForm;
import com.oreilly.servlet.ServletUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;
import java.util.Vector;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshminarayanan
 */
public class OnlineUsersAction extends org.apache.struts.action.Action {

    /* forward name="success" path="" */
    private static final String SUCCESS = "success";
    private static final String TASKPROCESS = "taskProcess";
    private static final String REMOVEONLINEUSERS = "removeOnlineUsers";
    private static final String EXPORT = "exportToExcel";

    /**
     * This is the action called from the Struts framework.
     * @param mapping The ActionMapping used to select this instance.
     * @param form The optional ActionForm bean for this request.
     * @param request The HTTP Request we are processing.
     * @param response The HTTP Response we are processing.
     * @throws java.lang.Exception
     * @return
     */
    @Override
    public ActionForward execute(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        OnlineUsersForm onlineUsersForm = (OnlineUsersForm) form;
        String buttonValue = onlineUsersForm.getButtonValue();
        String forward = "";
        if (CommonUtils.isEqual(buttonValue, "systemTaskProcess")) {
            request.setAttribute("systemTaskList", SystemTaskListProcess.listRunningProcesses());
            forward = TASKPROCESS;
        } else if (CommonUtils.isEqual(buttonValue, REMOVEONLINEUSERS)) {
            Integer removeUserId = onlineUsersForm.getUserId();
            if (null != removeUserId) {
                LoginUser loginUser = (LoginUser) this.getServlet().getServletContext().getAttribute("loginUser");
                DBUtil dbUtil = new DBUtil();
                dbUtil.tabProcessInfo(removeUserId);
                ArBatchDAO arBatchDAO = new ArBatchDAO();
                arBatchDAO.unLockBatchforUser(removeUserId);
                loginUser.removeUser(removeUserId.toString());
                this.getServlet().getServletContext().setAttribute("loginUser", loginUser);
            }
            request.setAttribute("onlineUsers", getUsersOnline(request));
            forward = SUCCESS;
        } else if (CommonUtils.isEqual(buttonValue, EXPORT)) {
            String excelFileName = this.exportUsersToExcel(getUsersOnline(request));
            if(CommonUtils.isNotEmpty(excelFileName)){
                response.addHeader("Content-Disposition", "attachment; filename=" + FilenameUtils.getName(excelFileName));
                response.setContentType("application/vnd.ms-excel" + ";charset=utf-8");
                ServletUtils.returnFile(excelFileName, response.getOutputStream());
                return null;
            }
            request.setAttribute("onlineUsers", getUsersOnline(request));
            forward = SUCCESS;
        } else {
            request.setAttribute("onlineUsers", getUsersOnline(request));
            forward = SUCCESS;
        }
        return mapping.findForward(forward);
    }

    private List<UserObjectBean> getUsersOnline(HttpServletRequest request) throws Exception {
        LoginUser loginUser = (LoginUser) this.getServlet().getServletContext().getAttribute("loginUser");
        Hashtable loggedUsers = loginUser.allElements();
        Hashtable loginTimes = loginUser.getLoginTimes();
        List<UserObjectBean> onlineUsers = new ArrayList<UserObjectBean>();
        Set<String> userIdSet = loggedUsers.keySet();
        UserDAO userDAO = new UserDAO();
        Vector userVector = null;
        User user = null;
        UserObjectBean userobjBean = null;
        for (String userId : userIdSet) {
            userVector = (Vector) loggedUsers.get(userId);
            userobjBean = new UserObjectBean();
            user = userDAO.findById(Integer.parseInt(userId));
            PropertyUtils.copyProperties(userobjBean, user);
            userobjBean.setUserIpAddress(userVector.get(1).toString());
            String terminal = userobjBean.getTerminal().getTrmnum()
                    + " - " + userobjBean.getTerminal().getTerminalLocation();
            userobjBean.setTerm(terminal);
            userobjBean.setLoginTime((Date)loginTimes.get(userId));
            userobjBean.setDate();
            onlineUsers.add(userobjBean);
        }
        return onlineUsers;
    }

    public String exportUsersToExcel(List<UserObjectBean> onlineUserList) throws Exception {
        String excelFileName = LoadLogisoftProperties.getProperty("reportLocation") + "/Documents/OnlineUsers/" + DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        File file = new File(excelFileName);
        if (!file.exists()) {
            file.mkdirs();
        }
        excelFileName += "Online Users.xls";
        return new ExportUsersOnlineToExcel().exportToExcel(excelFileName, onlineUserList);
    }
}
