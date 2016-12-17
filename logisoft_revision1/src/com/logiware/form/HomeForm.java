package com.logiware.form;

import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.logiware.accounting.model.NotesModel;
import com.logiware.bean.ItemsForRoleBean;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

/**
 *
 * @author Lakshmi Narayanan
 */
public class HomeForm extends ActionForm {

    private String action;
    private TreeMap<String, LinkedHashMap<Object, Object>> menus;
    private String terminalInfo;
    private String userName;
    private String roleName;
    private Integer itemId;
    private String selectedMenu;
    private TreeMap<Integer, ItemsForRoleBean> tabs;
    private String tabName;
    private String recordId;
    private String moduleId;
    private String loginName;
    private User user;
    private String screenName;
    private Integer followUpTaskId;
    private String customerNumber;
    private String showNotes = "next3days";

    //Export Voyage for goback to particular page option 
    private String pickVoyId;
    private String detailId;
    private String filterByChanges;
    private String callBack;
    private String unitSsId;
    private String toScreenName;
    private Long consolidateId;
    private String fromScreen;
    private String inTransitDr;
    private String fileNumber;
    private int userId;
    private String lclFileType;
    private String fclFileType;
    private String module;
    private String reference;
    private String accessMode;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public TreeMap<String, LinkedHashMap<Object, Object>> getMenus() {
        return menus;
    }

    public void setMenus(TreeMap<String, LinkedHashMap<Object, Object>> menus) {
        this.menus = menus;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getTerminalInfo() {
        return terminalInfo;
    }

    public void setTerminalInfo(String terminalInfo) {
        this.terminalInfo = terminalInfo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getSelectedMenu() {
        return selectedMenu;
    }

    public void setSelectedMenu(String selectedMenu) {
        this.selectedMenu = selectedMenu;
    }

    public TreeMap<Integer, ItemsForRoleBean> getTabs() {
        return tabs;
    }

    public void setTabs(TreeMap<Integer, ItemsForRoleBean> tabs) {
        this.tabs = tabs;
    }

    public String getTabName() {
        return tabName;
    }

    public void setTabName(String tabName) {
        this.tabName = tabName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public Integer getFollowUpTaskId() {
        return followUpTaskId;
    }

    public void setFollowUpTaskId(Integer followUpTaskId) {
        this.followUpTaskId = followUpTaskId;
    }

    public String getCustomerNumber() {
        return customerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        this.customerNumber = customerNumber;
    }

    public String getShowNotes() {
        return showNotes;
    }

    public void setShowNotes(String showNotes) {
        this.showNotes = showNotes;
    }

    public List<NotesModel> getFollowUpTasks() throws Exception {
        return new NotesDAO().getFollowUpTasks(user, showNotes);
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    public String getPickVoyId() {
        return pickVoyId;
    }

    public void setPickVoyId(String pickVoyId) {
        this.pickVoyId = pickVoyId;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    public String getFilterByChanges() {
        return filterByChanges;
    }

    public void setFilterByChanges(String filterByChanges) {
        this.filterByChanges = filterByChanges;
    }

    public String getCallBack() {
        return callBack;
    }

    public void setCallBack(String callBack) {
        this.callBack = callBack;
    }

    public String getUnitSsId() {
        return unitSsId;
    }

    public void setUnitSsId(String unitSsId) {
        this.unitSsId = unitSsId;
    }

    public String getToScreenName() {
        return toScreenName;
    }

    public void setToScreenName(String toScreenName) {
        this.toScreenName = toScreenName;
    }

    public Long getConsolidateId() {
        return consolidateId;
    }

    public void setConsolidateId(Long consolidateId) {
        this.consolidateId = consolidateId;
    }

    public String getFromScreen() {
        return fromScreen;
    }

    public void setFromScreen(String fromScreen) {
        this.fromScreen = fromScreen;
    }

    public String getInTransitDr() {
        return inTransitDr;
    }

    public void setInTransitDr(String inTransitDr) {
        this.inTransitDr = inTransitDr;
    }

    public String getFileNumber() {
        return fileNumber;
    }

    public void setFileNumber(String fileNumber) {
        this.fileNumber = fileNumber;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLclFileType() {
        return lclFileType;
    }

    public void setLclFileType(String lclFileType) {
        this.lclFileType = lclFileType;
    }

    public String getFclFileType() {
        return fclFileType;
    }

    public void setFclFileType(String fclFileType) {
        this.fclFileType = fclFileType;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getAccessMode() {
        return accessMode;
    }

    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }

    @Override
    public void reset(ActionMapping mapping, HttpServletRequest request) {
        user = (User) request.getSession().getAttribute("loginuser");
    }
}
