package com.logiware.action;

import com.gp.cong.common.CommonUtils;
import com.gp.cong.lcl.common.constant.LclCommonConstant;
import com.gp.cong.lcl.dwr.LclSession;
import com.gp.cong.logisoft.bc.fcl.ImportBc;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.Notes;
import com.gp.cong.logisoft.domain.RefTerminal;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.domain.lcl.LclBooking;
import com.gp.cong.logisoft.domain.lcl.LclQuote;
import com.gp.cong.logisoft.domain.lcl.LclUnitSs;
import com.gp.cong.logisoft.domain.lcl.bl.LclBl;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemTreeDAO;
import com.gp.cong.logisoft.hibernate.dao.NotesDAO;
import com.gp.cong.logisoft.hibernate.dao.ProcessInfoDAO;
import com.gp.cong.logisoft.hibernate.dao.TradingPartnerDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLBookingDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LCLQuoteDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclFileNumberDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclSsHeaderDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.LclUnitSsDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.SearchDAO;
import com.gp.cong.logisoft.hibernate.dao.lcl.bl.LCLBlDAO;
import com.gp.cong.logisoft.util.CommonFunctions;
import com.logiware.bean.ItemsForRoleBean;
import com.logiware.constants.ItemConstants;
import com.logiware.form.HomeForm;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import com.gp.cong.logisoft.util.DBUtil;
import com.gp.cong.struts.LoadLogisoftProperties;
import com.gp.cvst.logisoft.hibernate.dao.SystemRulesDAO;
import com.logiware.hibernate.dao.FclDAO;
import java.util.LinkedHashMap;

/**
 *
 * @author Lakshmi Narayanan
 */
public class HomeAction extends DispatchAction {

    private static final String FOLLOW_UP_TASKS = "followUpTasks";
    private static final String SERVER_STATUS = "serverStatus";
    private boolean homeScreenFileFlag = Boolean.FALSE;

    public ActionForward showHome(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        if (null != loginUser && null != loginUser.getLoginName()) {
            homeForm.setLoginName(loginUser.getLoginName());
            homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        }
        Integer roleId = null;
        if (null != loginUser && null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser && null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        if (CommonUtils.isNotEmpty(homeForm.getFileNumber()) && !"undefined".equalsIgnoreCase(homeForm.getFileNumber())) {
            if (CommonUtils.isNotEmpty(homeForm.getModuleId()) && "BOOKING".equalsIgnoreCase(homeForm.getModuleId())) {
                new DBUtil().releaseLockByRecordIdAndModuleId(homeForm.getFileNumber(), "BOOKING", loginUser.getUserId());
            } else if (CommonUtils.isNotEmpty(homeForm.getModuleId()) && "QUOTE".equalsIgnoreCase(homeForm.getModuleId())) {
                new DBUtil().releaseLockByRecordIdAndModuleId(homeForm.getFileNumber(), "QUOTE", loginUser.getUserId());
            } else {
                new DBUtil().releaseLockByRecordIdAndModuleId(homeForm.getFileNumber(), "LCL FILE", loginUser.getUserId());
            }
        }
        homeForm.setMenus(getMenus(roleId));
        request.getSession().setAttribute("menus", homeForm.getMenus());
        return mapping.findForward("success");
    }

    public ActionForward showTabs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        setMenusFromSession(request.getSession(), homeForm);
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setSelectedMenu(homeForm.getSelectedMenu());
        session.setAttribute("lclSession", lclSession);
        request.setAttribute("loginUser", loginUser);
        Integer roleId = null;
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        if (CommonUtils.isNotEmpty(homeForm.getItemId())) {
            homeForm.setTabs(getTabs(homeForm, request, roleId));
        }

        //remove the file lock here
        if (CommonUtils.isNotEmpty(homeForm.getRecordId()) && CommonUtils.isNotEmpty(homeForm.getModuleId()) && null != loginUser) {
            new DBUtil().releaseLockByRecordIdAndModuleId(homeForm.getRecordId(), homeForm.getModuleId(), loginUser.getUserId());
        } else if (CommonUtils.isNotEmpty(homeForm.getRecordId())) {
            new ProcessInfoDAO().releaseFclOpsFile(homeForm.getRecordId(), loginUser.getUserId());// Only for fcl ops files
        }
        session.setAttribute("followupFlag", Boolean.FALSE);
        return mapping.findForward("success");
    }

    public ActionForward changeTabs(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession")
                ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        lclSession.setSelectedMenu(homeForm.getSelectedMenu());
        session.setAttribute("lclSession", lclSession);
        Integer roleId = null;
        ItemDAO itemDAO = new ItemDAO();
        String fileNumber = "";
        homeForm = this.lclSetItemId(homeForm, request);
        setMenusFromSession(request.getSession(), homeForm);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        if (CommonUtils.isNotEmpty(homeForm.getItemId())) {
            Item parentItem = itemDAO.findById(homeForm.getItemId());
            if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_CN_POOL)
                    || ("LCLI DR".equalsIgnoreCase(request.getParameter("callBack"))
                    && CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_BL_INVOICE_POOL))) {
                parentItem = itemDAO.findItemNameUC(ItemConstants.LCL_BOOKINGS_QUOTES_IMPORTS, ItemConstants.LCL_BOOKINGS_QUOTES_IMPORTS_UC);
                homeForm.setItemId(parentItem.getItemId());
            }
            if ("OPEN IMP VOYAGE".equalsIgnoreCase(request.getParameter("callBack"))) {
                parentItem = itemDAO.findItemNameUC(ItemConstants.LCL_IMPORT_VOYAGE_MENU, ItemConstants.LCL_IMPORT_VOYAGE_CODE);
                homeForm.setItemId(parentItem.getItemId());
                homeForm.setTabs(getTabsForLclImportVoyage(homeForm, request, roleId));
                if (CommonUtils.isNotEmpty(homeForm.getRecordId())) {
                    fileNumber = new LclFileNumberDAO().getFileNumberByFileId(homeForm.getRecordId());
                }
                if (CommonUtils.isNotEmpty(fileNumber) && null != loginUser) {
                    new DBUtil().releaseLockByRecordIdAndModuleId(fileNumber, "LCL FILE", loginUser.getUserId());
                }
            } else if ("EXPORT_TO_IMPORT".equalsIgnoreCase(homeForm.getToScreenName())) {
                parentItem = itemDAO.findItemNameUC(ItemConstants.LCL_BOOKINGS_QUOTES, ItemConstants.LCL_EXPORT_BOOKING_CODE);
                homeForm.setItemId(parentItem.getItemId());
                homeForm.setTabs(getTabsForLclExportVoyage(homeForm, request, roleId));
            } else if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.BOOKINGS_QUOTES_BL_EXPORT)) {
                homeForm.setTabs(getTabsForBookingsQuotesFclBlExport(homeForm, request, roleId));
            } else if ((CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_BOOKINGS_QUOTES)
                    || CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_BOOKINGS_QUOTES_IMPORTS))
                    && !"EXP VOYAGE".equalsIgnoreCase(homeForm.getToScreenName())) {
                homeForm.setTabs(getTabsForLclBookingsQuotesExport(homeForm, request, roleId));
            } else if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_BL_INVOICE_POOL)
                    && "IMP VOYAGE".equalsIgnoreCase(request.getParameter("callBack"))) {
                parentItem = itemDAO.findItemNameUC(ItemConstants.LCL_IMPORT_VOYAGE_MENU, ItemConstants.LCL_IMPORT_VOYAGE_CODE);
                homeForm.setItemId(parentItem.getItemId());
                homeForm.setTabs(getTabsForLclImportVoyage(homeForm, request, roleId));
            } else if ("EXP VOYAGE".equalsIgnoreCase(homeForm.getToScreenName())) {
                if ("UnitLoadScreenToBooking".equalsIgnoreCase(homeForm.getFromScreen())
                        || "UnitViewDrScreenToBooking".equalsIgnoreCase(homeForm.getFromScreen())) {
                    parentItem = itemDAO.findItemNameUC(ItemConstants.LCL_BOOKINGS_QUOTES, ItemConstants.LCL_EXPORT_BOOKING_CODE);
                } else if ("BookingToUnitLoadScreen".equalsIgnoreCase(homeForm.getFromScreen())
                        || "BookingToUnitViewDrScreen".equalsIgnoreCase(homeForm.getFromScreen())
                        || "DISPUTED_MASTER_BL".equalsIgnoreCase(homeForm.getFromScreen())) {
                    parentItem = itemDAO.findItemNameUC(ItemConstants.LCL_UNIT_SCHEDULE_EXPORT, ItemConstants.LCL_EXPORT_VOYAGE_CODE);
                }
                homeForm.setItemId(parentItem.getItemId());
                homeForm.setTabs(getTabsForLclExportVoyage(homeForm, request, roleId));
            } else if ("DISPUTED_MASTER_BL".equalsIgnoreCase(homeForm.getToScreenName())) {
                parentItem = itemDAO.findItemNameUC("SS Masters Disputed", "LCLESSMAD");
                homeForm.setItemId(parentItem.getItemId());
                homeForm.setTabs(getTabsForLclExportVoyage(homeForm, request, roleId));
            } else if ("LCLBooking".equalsIgnoreCase(homeForm.getToScreenName())
                    && "ar_inquiry".equalsIgnoreCase(homeForm.getFromScreen())) {
                parentItem = itemDAO.findItemNameUC("Quotes, Bookings AND BLs", "LCLQBBL");
                homeForm.setItemId(parentItem.getItemId());
                homeForm.setTabs(getTabsForLclExportVoyage(homeForm, request, roleId));
            } else if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_IMP_DOOR_DELIVERY_POOL)) {
                homeForm.setTabs(getTabsForLclBookingsQuotesExport(homeForm, request, roleId));
            } else {
                homeForm.setTabs(getTabs(homeForm, request, roleId));
            }
        }
        //release Lock LCL Record
        if (CommonUtils.isNotEmpty(homeForm.getRecordId()) && null != loginUser) {
            fileNumber = new LclFileNumberDAO().getFileNumberByFileId(homeForm.getRecordId().toString());
            new DBUtil().releaseLockByRecordIdAndModuleId(fileNumber, "LCL FILE", loginUser.getUserId());
        }
        //release Lock LCL Export Conoslidated Record
        if (CommonUtils.isNotEmpty(homeForm.getConsolidateId()) && null != loginUser) {
            fileNumber = new LclFileNumberDAO().getFileNumberByFileId(homeForm.getConsolidateId().toString());
            new DBUtil().releaseLockByRecordIdAndModuleId(fileNumber, "LCL FILE", loginUser.getUserId());
        }
        if (CommonUtils.isNotEmpty(homeForm.getRecordId()) && "EXP VOYAGE".equalsIgnoreCase(homeForm.getToScreenName())
                && null != loginUser) {
            fileNumber = new LclFileNumberDAO().getFileNumberByFileId(homeForm.getRecordId().toString());
            new DBUtil().releaseLockByRecordIdAndModuleId(fileNumber, "LCL FILE", loginUser.getUserId());
        }
        request.setAttribute("fileNumber", request.getParameter("recordId"));
        session.setAttribute("followupFlag", Boolean.FALSE);
        return mapping.findForward("success");
    }

    public ActionForward changeTabsByTabName(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        Integer roleId = null;
        setMenusFromSession(request.getSession(), homeForm);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        if (CommonUtils.isNotEmpty(homeForm.getItemId())) {
            homeForm.setTabs(getTabsByTabName(homeForm, request, roleId));
        }
        return mapping.findForward("success");
    }

    private TreeMap<String, LinkedHashMap<Object, Object>> getMenus(Integer roleId) throws Exception {
        TreeMap<String, LinkedHashMap<Object, Object>> menus = new TreeMap<String, LinkedHashMap<Object, Object>>();
        List<Item> menuItems = new ItemDAO().getMenu();
        boolean hasSubMenu = false;
        boolean hasSubItem = false;
        for (Item menu : menuItems) {
            List<Item> subMenuItems = new ItemDAO().getSubMenuWitoutTP(menu.getItemId());
            LinkedHashMap subMenu = new LinkedHashMap();
            for (Item subMenuItem : subMenuItems) {
                List<ItemsForRoleBean> tabs = new ItemDAO().getItemsForRole(subMenuItem.getItemId(), roleId);
                if (CommonUtils.isNotEmpty(tabs)) {
                    hasSubMenu = true;
                    subMenu.put("" + subMenuItem.getItemId(), subMenuItem.getItemDesc());
                } else {
                    List<Item> subMenuList = new ItemDAO().getSubMenuWitoutTP(subMenuItem.getItemId());
                    if (CommonUtils.isNotEmpty(subMenuList)) {
                        TreeMap<Integer, String> subItems = new TreeMap<Integer, String>();
                        for (Item item : subMenuList) {
                            List<ItemsForRoleBean> subMenuTabs = new ItemDAO().getItemsForRole(item.getItemId(), roleId);
                            if (CommonUtils.isNotEmpty(subMenuTabs)) {
                                hasSubItem = true;
                                subItems.put(item.getItemId(), item.getItemDesc());
                            }
                        }
                        if (hasSubItem && !subItems.isEmpty()) {
                            hasSubMenu = true;
                            subMenu.put(subMenuItem.getItemDesc(), subItems);
                        }
                    }
                }
            }
            if (hasSubMenu && !subMenu.isEmpty()) {
                //add the Intra Inbound item to top of Exports
                if ("Exports".equalsIgnoreCase(menu.getItemDesc())) {
                    Item inttraSI = new ItemDAO().getInttraInboundSIMenu();
                    if (null != inttraSI) {
                        /* List<ItemsForRoleBean> tabs = new ItemDAO().getItemsForRole(inttraSI.getItemId(), roleId);
                         if (CommonUtils.isNotEmpty(tabs)) {
                         subMenu.put("" + inttraSI.getItemId(), inttraSI.getItemDesc());
                         }*/
                        List<Item> subMenuList = new ItemDAO().getSubMenuWitoutTP(inttraSI.getItemId());
                        if (CommonUtils.isNotEmpty(subMenuList)) {
                            TreeMap<Integer, String> subItems = new TreeMap<Integer, String>();
                            for (Item item : subMenuList) {
                                List<ItemsForRoleBean> subMenuTabs = new ItemDAO().getItemsForRole(item.getItemId(), roleId);
                                if (CommonUtils.isNotEmpty(subMenuTabs)) {
                                    hasSubItem = true;
                                    subItems.put(item.getItemId(), item.getItemDesc());
                                }
                            }
                            if (hasSubItem && !subItems.isEmpty()) {
                                hasSubMenu = true;
                                subMenu.put(inttraSI.getItemDesc(), subItems);
                            }
                        }
                    }
                }
                if (!"Utilities".equalsIgnoreCase(menu.getItemDesc())) {
                    //add the trading partner item to bottom of all the menu
                    Item tp = new ItemDAO().getTradingPartnerMenu();
                    if (null != tp) {
                        List<ItemsForRoleBean> tabs = new ItemDAO().getItemsForRole(tp.getItemId(), roleId);
                        if (CommonUtils.isNotEmpty(tabs)) {
                            subMenu.put("" + tp.getItemId(), tp.getItemDesc());
                        }
                    }
                }
                menus.put(menu.getItemDesc(), subMenu);
            }
        }
        return menus;
    }

    private TreeMap<Integer, ItemsForRoleBean> getTabs(HomeForm homeForm,
            HttpServletRequest request, Integer roleId) throws Exception {
        HttpSession session = request.getSession();
        LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        Item parentItem = new ItemDAO().findById(homeForm.getItemId());
        List<ItemsForRoleBean> itemsForRole = new ItemDAO().getItemsForRole(homeForm.getItemId(), roleId);
        int tabIndex = 0;
        for (ItemsForRoleBean tabItem : itemsForRole) {
            String program = tabItem.getProgram();
            String queryString = "?";
            if (program.contains("?")) {
                queryString = "&";
            }
            if (program.endsWith("?")) {
                queryString = "";
            }
            if (!program.startsWith("/")) {
                program = "/";
            }
            int quoteAccessMode = 0;
            int bookingAccessMode = 0;
            int blAccessMode = 0;
            Integer accessMode = 0;
            if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                accessMode = 1;
            }
            StringBuilder src = new StringBuilder(request.getContextPath());
            src.append(program).append(queryString);
            src.append("modify=").append(accessMode);
            src.append("&programid=").append(tabItem.getItemId());
            src.append("&date=").append(new Date());
            if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.BOOKINGS_QUOTES_BL_EXPORT)) {
                String companyCode = new SystemRulesDAO().getSystemRules("CompanyCode");
                session.setAttribute("companyMnemonicCode", new SystemRulesDAO().getSystemRules("CompanyNameMnemonic"));
                if ("02".equals(companyCode)) {
                    session.setAttribute("companyCode", "OTIC");
                } else {
                    session.setAttribute("companyCode", "ECCI");
                }
                if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.FCL_SEARCH_EXPORT)) {
                    src.append("&accessMode=").append(accessMode);
                    if (CommonUtils.isNotEmpty(new ItemTreeDAO().getAccessModeByTabName("Quotes", roleId, "QOT"))) {
                        session.setAttribute("quoteAccessMode", true);
                    } else {
                        session.setAttribute("quoteAccessMode", false);
                    }
                    session.setAttribute("bookingAccessMode", false);
                    session.setAttribute("blAccessMode", false);
                    tabItem.setSrc(src.toString());
                    if (null != session.getAttribute(ImportBc.sessionName)) {
                        session.removeAttribute(ImportBc.sessionName);
                        if (null != session.getAttribute("oldSearchForm")) {
                            session.removeAttribute("oldSearchForm");
                        }
                    }
                    tabs.put(0, tabItem);
                    break;
                } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.FILE_SEARCH_IMPORT)) {
                    if (CommonUtils.isNotEmpty(new ItemTreeDAO().getAccessModeByTabName("IMPORT QUOTE", roleId, "IMPQOT"))) {
                        session.setAttribute("quoteAccessMode", true);
                    } else {
                        session.setAttribute("quoteAccessMode", false);
                    }
                    if (CommonUtils.isNotEmpty(new ItemTreeDAO().getAccessModeByTabName("IMPORT BOOKING", roleId, "IMPBOK"))) {
                        session.setAttribute("bookingAccessMode", true);
                    } else {
                        session.setAttribute("bookingAccessMode", false);
                    }
                    if (CommonUtils.isNotEmpty(new ItemTreeDAO().getAccessModeByTabName("IMPORT FCL BL", roleId, "IMPBL"))) {
                        session.setAttribute("blAccessMode", true);
                    } else {
                        session.setAttribute("blAccessMode", false);
                    }
                    src.append("&accessMode=").append(accessMode);
                    tabItem.setSrc(src.toString());
                    if (null == session.getAttribute(ImportBc.sessionName)) {
                        if (null != session.getAttribute("oldSearchForm")) {
                            session.removeAttribute("oldSearchForm");
                        }
                    }
                    session.setAttribute(ImportBc.sessionName, ImportBc.sessionValue);
                    tabs.put(0, tabItem);
                    break;
                }
            } else if (lclSession.getSelectedMenu() != null && lclSession.getSelectedMenu().equalsIgnoreCase("Exports") && CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_BOOKINGS_QUOTES)) {
                if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SEARCH_EXPORT)) {
                    src.append("&accessMode=").append(accessMode);
                    if (CommonUtils.isNotEmpty(new ItemTreeDAO().getAccessModeByTabName("Quotes", roleId, "LCLQ"))) {
                        quoteAccessMode = 1;
                    }
                    session.setAttribute("quoteAccessMode", quoteAccessMode);
                    tabItem.setSrc(src.toString());
                    tabs.put(0, tabItem);
                    break;
                }
            } else if (lclSession.getSelectedMenu() != null && lclSession.getSelectedMenu().equalsIgnoreCase("Imports") && CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_BOOKINGS_QUOTES_IMPORTS)) {
                if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SEARCH_IMPORT)) {
                    src.append("&accessMode=").append(accessMode);
                    if (CommonUtils.isNotEmpty(new ItemTreeDAO().getAccessModeByTabName("Quotes", roleId, "LCLIQ"))) {
                        quoteAccessMode = 1;
                    }
                    session.setAttribute("quoteAccessMode", quoteAccessMode);
                    tabItem.setSrc(src.toString());
                    tabs.put(0, tabItem);
                    break;
                }
            } else if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.LCL_UNIT_SCHEDULE_EXPORT)
                    && "LCLUS".equalsIgnoreCase(parentItem.getUniqueCode())) {
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex, tabItem);
            } else {
                if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), homeForm.getTabName())) {
                    request.setAttribute("selectedTab", tabIndex);
                }
                if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), "BL Correction Notice Pool")) {
                    if (CommonUtils.isEqualIgnoreCase(parentItem.getUniqueCode(), "COR")) {
                        session.removeAttribute(ImportBc.sessionName);
                    } else {
                        session.setAttribute(ImportBc.sessionName, ImportBc.sessionValue);
                    }
                }
                src.append("&accessMode=").append(accessMode);
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex, tabItem);
            }
            tabIndex++;
        }
        return tabs;
    }

    private TreeMap<Integer, ItemsForRoleBean> getTabsByTabName(HomeForm homeForm,
            HttpServletRequest request, Integer roleId) throws Exception {
        HttpSession session = request.getSession();
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        Item parentItem = new ItemDAO().findById(homeForm.getItemId());
        StringBuilder tabName = new StringBuilder();
        StringTokenizer tokenizer = new StringTokenizer(homeForm.getTabName(), ":-");
        while (tokenizer.hasMoreTokens()) {
            tabName.append("'").append(tokenizer.nextToken()).append("',");
        }

        List<ItemsForRoleBean> itemsForRole = new ItemDAO().getItemsForRoleByDesc(homeForm.getItemId(), roleId, StringUtils.removeEnd(tabName.toString(), ","));
        int index = 0;
        for (ItemsForRoleBean tabItem : itemsForRole) {
            String program = tabItem.getProgram();
            String queryString = "?";
            if (program.contains("?")) {
                queryString = "&";
            }
            if (program.endsWith("?")) {
                queryString = "";
            }
            if (!program.startsWith("/")) {
                program = "/";
            }
            Integer accessMode = 0;
            if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                accessMode = 1;
            }
            StringBuilder src = new StringBuilder(request.getContextPath());
            src.append(program).append(queryString);
            src.append("modify=").append(accessMode);
            src.append("&programid=").append(tabItem.getItemId());
            src.append("&accessMode=").append(accessMode);
            src.append("&date=").append(new Date());
            tabItem.setSrc(src.toString());
            if (CommonUtils.isEqualIgnoreCase(parentItem.getItemDesc(), ItemConstants.BOOKINGS_QUOTES_BL_EXPORT)) {
                if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.FCL_SEARCH_EXPORT)) {
                    tabs.put(0, tabItem);
                    break;
                } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.FILE_SEARCH_IMPORT)) {
                    tabs.put(0, tabItem);
                    break;
                }
            } else {
                tabs.put(index, tabItem);
            }
            index++;
        }
        return tabs;
    }

    private TreeMap<Integer, ItemsForRoleBean> getTabsForLclBookingsQuotesExport(HomeForm homeForm,
            HttpServletRequest request, Integer roleId) throws Exception {
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        List<ItemsForRoleBean> itemsForRole = new ItemDAO().getItemsForRole(homeForm.getItemId(), roleId);
        HttpSession session = request.getSession();
        int tabIndex = 0;
        String fileNumber = "";
        Long fileNumberId = 0l;
        if (null != request.getParameter("recordId") && !request.getParameter("recordId").equals("")) {
            fileNumber = new LclFileNumberDAO().getFileNumberByFileId(request.getParameter("recordId"));
            fileNumberId = Long.parseLong(request.getParameter("recordId"));
        }
        String moduleId = request.getParameter("moduleId");
        if (CommonUtils.isEmpty(fileNumber)) {
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setCommodityList(null);
            lclSession.setQuoteCommodityList(null);
            lclSession.setQuoteAcList(null);
        }
        if (homeForm.getSelectedMenu().equalsIgnoreCase("Exports")) {
            session.setAttribute("companyMnemonicCode", null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic") ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "");
        } else {
            session.setAttribute("companyMnemonicCode", null != LoadLogisoftProperties.getProperty("application.fclBl.print.companyName") ? LoadLogisoftProperties.getProperty("application.fclBl.print.companyName").toUpperCase() : "");
            session.setAttribute("applicationEmailCompanyName", null != LoadLogisoftProperties.getProperty("application.email.companyName") ? LoadLogisoftProperties.getProperty("application.email.companyName").toUpperCase() : "");
        }
        LclQuote lclQuote = null;
        LclBl lclBl = null;
        LclBooking lclBooking = null;
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        LCLBlDAO lclBlDAO = new LCLBlDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        int tabIndexQuote = 0;
        int tabIndexBkg = 0;
        int tabIndexBl = 0;
        Boolean moduleFlag = LclCommonConstant.LCL_IMPORT.equalsIgnoreCase(homeForm.getSelectedMenu()) ? true : false;
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            lclQuote = lclQuoteDAO.findById(fileNumberId);
            lclBl = moduleFlag ? null : lclBlDAO.findById(fileNumberId);
            lclBooking = lclBookingDAO.findById(fileNumberId);
            /* consolidation check */
            if (null == lclBl && !"B".equalsIgnoreCase(moduleId) && !moduleFlag) { // for only when convert to bl and pick consolidate.
                Long consolidateBlId = lclBlDAO.findConsolidateBl(fileNumberId);
                if (null != consolidateBlId) {
                    lclBl = lclBlDAO.findById(consolidateBlId);
                }
            }
            /* ends */
            if (lclQuote != null && lclBl != null && lclBooking != null) {
                tabIndexQuote = 0;
                tabIndexBkg = 2;
                tabIndexBl = 1;
            } else if (lclBl != null && lclBooking != null) {
                if (LclCommonConstant.LCL_EXPORT.equalsIgnoreCase(homeForm.getSelectedMenu())) {
                    tabIndexBkg = 1;
                    tabIndexBl = 0;
                    if ("converToBlFlag".equalsIgnoreCase(homeForm.getCallBack())) {
                        tabIndexBkg = 0;
                        tabIndexBl = 1;
                    }
                } else {
                    tabIndexBkg = 0;
                }
            } else if (lclBooking != null && lclQuote != null) {
                tabIndexQuote = 0;
                tabIndexBkg = 1;
            } else if (lclQuote != null) {
                tabIndexQuote = 0;
            } else if (lclBooking != null) {
                tabIndexBkg = 0;
            }
        }
        for (ItemsForRoleBean tabItem : itemsForRole) {
            String program = tabItem.getProgram();
            if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.QUOTES_EXPORT)) {
                if (moduleId.equals("Quotes") || moduleId.equals("Q") || moduleId.equals("B") || moduleId.equals("BL")) {
                    if ((moduleId.equals("B") || moduleId.equals("BL")) && lclQuote == null) {
                    } else {
                        if (CommonUtils.isNotEmpty(fileNumberId) && lclQuote != null) {
                            program = "/lclQuote.do?methodName=editQuote&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&moduleName=" + homeForm.getSelectedMenu() + "&homeScreenQtFileFlag=" + homeScreenFileFlag;
                        } else {
                            program = "/lclQuote.do?methodName=newQuote" + "&moduleName=" + homeForm.getSelectedMenu();
                        }
                        String queryString = "?";
                        if (program.contains("?")) {
                            queryString = "&";
                        }
                        if (program.endsWith("?")) {
                            queryString = "";
                        }
                        if (!program.startsWith("/")) {
                            program = "/";
                        }
                        Integer accessMode = 0;
                        if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                            accessMode = 1;
                        }
                        StringBuilder src = new StringBuilder(request.getContextPath());
                        src.append(program).append(queryString);
                        src.append("modify=").append(accessMode);
                        src.append("&programid=").append(tabItem.getItemId());
                        src.append("&accessMode=").append(accessMode);
                        src.append("&date=").append(new Date());
                        appendScanAttachQueryString(fileNumber, "LCL FILE", "Quote", "Scan or Attach", src);
                        tabItem.setSrc(src.toString());
                        tabs.put(tabIndex + 1, tabItem);
                        request.setAttribute("selectedTab", tabIndexQuote);
                        tabIndex++;
                    }
                }
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.BOOKINGS_EXPORT)
                    || CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), "CN Pool")) {
                if (moduleId.equals("Bookings") || moduleId.equals("B") || moduleId.equals("BL")) {
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        program = "/lclBooking.do?methodName=editBooking&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
                                + "&moduleName=" + homeForm.getSelectedMenu() + "&callBackFlag=" + request.getParameter("callBack")
                                + "&toScreenName=" + homeForm.getToScreenName() + "&fromScreen=" + homeForm.getFromScreen() + "&consolidatedId=" + homeForm.getConsolidateId() + "&homeScreenDrFileFlag=" + homeScreenFileFlag + "&tabName=" + homeForm.getTabName();
                    } else {
                        String shortShipFileNo = request.getParameter("shortShipfileNo");
                        if (CommonUtils.isNotEmpty(shortShipFileNo)) {
                            program = "/lclBooking.do?methodName=createShortShip&moduleName=" + homeForm.getSelectedMenu()
                                    + "&shortShip=" + shortShipFileNo + "&pickVoyId=" + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId()
                                    + "&filterByChanges=" + homeForm.getFilterByChanges() + "&unitSsId=" + homeForm.getUnitSsId()
                                    + "&toScreenName=" + homeForm.getToScreenName() + "&fromScreen=" + homeForm.getFromScreen() + "&inTransitDr=false";
                        } else {
                            program = "/lclBooking.do?methodName=newBooking" + "&moduleName=" + homeForm.getSelectedMenu();
                        }
                    }
                    String queryString = "?";
                    if (program.contains("?")) {
                        queryString = "&";
                    }
                    if (program.endsWith("?")) {
                        queryString = "";
                    }
                    if (!program.startsWith("/")) {
                        program = "/";
                    }
                    Integer accessMode = 0;
                    if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                        accessMode = 1;
                    }
                    StringBuilder src = new StringBuilder(request.getContextPath());
                    src.append(program).append(queryString);
                    src.append("modify=").append(accessMode);
                    src.append("&programid=").append(tabItem.getItemId());
                    src.append("&accessMode=").append(accessMode);
                    src.append("&date=").append(new Date());
                    appendScanAttachQueryString(fileNumber, "LCL FILE", "Booking", "Scan or Attach", src);
                    tabItem.setSrc(src.toString());
                    tabs.put(tabIndex + 1, tabItem);
                    request.setAttribute("selectedTab", tabIndexBkg);
                    tabIndex++;
                }
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SCHEDULE_SEARCH_EXPORT)) {
                if (moduleId.equals("Bookings") || moduleId.equals("B") || moduleId.equals("BL")) {
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        program = "/lclBooking.do?methodName=editBooking&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber + "&pickVoyId="
                                + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId() + "&filterByChanges="
                                + homeForm.getFilterByChanges() + "&unitSsId=" + homeForm.getUnitSsId() + "&toScreenName=" + homeForm.getToScreenName() + "&homeScreenDrFileFlag=" + homeScreenFileFlag;
                    } else {
                        program = "/lclBooking.do?methodName=newBooking";
                    }
                    String queryString = "?";
                    if (program.contains("?")) {
                        queryString = "&";
                    }
                    if (program.endsWith("?")) {
                        queryString = "";
                    }
                    if (!program.startsWith("/")) {
                        program = "/";
                    }
                    Integer accessMode = 0;
                    if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                        accessMode = 1;
                    }
                    StringBuilder src = new StringBuilder(request.getContextPath());
                    src.append(program).append(queryString);
                    src.append("modify=").append(accessMode);
                    src.append("&programid=").append(tabItem.getItemId());
                    src.append("&accessMode=").append(accessMode);
                    src.append("&date=").append(new Date());
                    appendScanAttachQueryString(fileNumber, "LCL FILE", "Booking", "Scan or Attach", src);
                    tabItem.setSrc(src.toString());
                    tabs.put(tabIndex + 1, tabItem);
                    request.setAttribute("selectedTab", tabIndex);
                    tabIndex++;
                }
            } else if (lclBl != null && CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_BL_EXPORT)
                    && LclCommonConstant.LCL_EXPORT.equalsIgnoreCase(homeForm.getSelectedMenu()) && moduleId.equals("BL")) {
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    program = "/lclBl.do?methodName=editBooking&fileNumberId=" + lclBl.getLclFileNumber().getId() + "&fileNumber="
                            + lclBl.getLclFileNumber().getFileNumber() + "&consolidatedId=" + homeForm.getConsolidateId()
                            + "&bookingFileNumberId=" + fileNumberId + "&homeScreenDrFileFlag=" + homeScreenFileFlag;
                } else {
                    program = "/lclBl.do?methodName=newBooking";
                }
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                src.append("&moduleName=").append(homeForm.getSelectedMenu());
                appendScanAttachQueryString(fileNumber, "LCL FILE", "Bl", "Scan or Attach", src);
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                request.setAttribute("selectedTab", tabIndexBl);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SEARCH_EXPORT) 
                    || CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_IMP_DOOR_DELIVERY_SEARCH)
                    ||(CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SEARCH_IMPORT))) {
                if (moduleId.equals("mainScreen")) {
                    LclSession lclSession = null != (LclSession) session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                    lclSession.setIsArGlmappingFlag(false);
                    if (lclSession.getSearchFileNo() != null && lclSession.getSearchFileNo().equalsIgnoreCase("Y") && lclSession.getSearchForm() != null && CommonUtils.isNotEmpty(homeForm.getScreenName()) && homeForm.getScreenName().equalsIgnoreCase("searchResult")) {
                        lclSession.getSearchForm().setFileNumber("");
                        lclSession.setSearchFileNo("");
                    }
                    if (CommonUtils.isEqual(lclSession.getConsolidated(), "true") && CommonUtils.isNotEmpty(fileNumberId)) {
                        program = "/lclSearch.do?methodName=consolidatedFiles&fileNumberA=" + lclSession.getConsolidatedFileId();
                    } else if (CommonUtils.isEqual(lclSession.getSearchResult(), "true")) {
                        if (LclCommonConstant.LCL_EXPORT.equalsIgnoreCase(homeForm.getSelectedMenu())) {
                            program = "/lclSearch.do?methodName=backToSearch";
                        } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_IMP_DOOR_DELIVERY_SEARCH)) {
                            program = "/LclDoorDeliverySearch.do?methodName=goBackToSearch";
                        } else {
                            program = "/lclImportSearch.do?methodName=backToSearch";
                        }
                        if (CommonUtils.isNotEmpty(fileNumberId)) {
                            program += "&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber;
                        }
                    } else {
                        program = "/lclBooking.do?methodName=backToMainScreen";
                    }
                    String queryString = "?";
                    if (program.contains("?")) {
                        queryString = "&";
                    }
                    if (program.endsWith("?")) {
                        queryString = "";
                    }
                    if (!program.startsWith("/")) {
                        program = "/";
                    }
                    Integer accessMode = 0;
                    if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                        accessMode = 1;
                    }
                    StringBuilder src = new StringBuilder(request.getContextPath());
                    src.append(program).append(queryString);
                    src.append("modify=").append(accessMode);
                    src.append("&programid=").append(tabItem.getItemId());
                    src.append("&accessMode=").append(accessMode);
                    src.append("&date=").append(new Date());
                    src.append("&moduleName=").append(homeForm.getSelectedMenu());
                    tabItem.setSrc(src.toString());
                    tabs.put(tabIndex + 1, tabItem);
                    request.setAttribute("selectedTab", tabIndex);
                    tabIndex++;
                }
            }
        }
        return tabs;
    }

    private TreeMap<Integer, ItemsForRoleBean> getTabsForLclImportVoyage(HomeForm homeForm,
            HttpServletRequest request, Integer roleId) throws Exception {
        LclUnitSsDAO lclunitSsDAO = new LclUnitSsDAO();
        LclUnitSs lclUnitSs = null;
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        List<ItemsForRoleBean> itemsForRole = new ItemDAO().getItemsForRole(homeForm.getItemId(), roleId);
        int tabIndex = 0;
        Long headerId = null;
        Long unitSsId = null;
        Long unitId = null;
        if ("OPEN IMP VOYAGE".equalsIgnoreCase(request.getParameter("callBack"))) {
            unitSsId = CommonUtils.isNotEmpty(request.getParameter("unitSsId")) ? Long.parseLong(request.getParameter("unitSsId")) : 0;
        } else {
            unitSsId = CommonUtils.isNotEmpty(request.getParameter("recordId")) ? Long.parseLong(request.getParameter("recordId")) : 0;
        }
        lclUnitSs = lclunitSsDAO.findById(unitSsId);
        if (lclUnitSs != null) {
            headerId = lclUnitSs.getLclSsHeader().getId();
            unitId = lclUnitSs.getLclUnit().getId();
        }
        for (ItemsForRoleBean tabItem : itemsForRole) {
            String program = tabItem.getProgram();
            if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_IMPORT_VOYAGE_TAB)) {
                if (CommonUtils.isNotEmpty(headerId)) {
                    program = "/lclImportAddVoyage.do?methodName=editVoyage&headerId=" + headerId + "&unitId=" + unitId + "&homeScreenVoyageFileFlag=" + homeScreenFileFlag;
                }
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                tabIndex++;
            }
        }
        return tabs;
    }

    private TreeMap<Integer, ItemsForRoleBean> getTabsForBookingsQuotesFclBlExport(HomeForm homeForm,
            HttpServletRequest request, Integer roleId) throws Exception {
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        List<ItemsForRoleBean> itemsForRole = new ItemDAO().getItemsForRole(homeForm.getItemId(), roleId);
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        int tabIndex = 0;
        String bookingId = "";
        String blId = "";
        String quoteId = request.getParameter("quoteId");
        String fileNumber = request.getParameter("fileNumber");
        if (CommonFunctions.isNotNull(fileNumber) && fileNumber.indexOf("-") > -1) {
            bookingId = request.getParameter("bookingId");
            blId = request.getParameter("blId");
        } else {
            Object[] bookingAndBlIds = new ItemDAO().getBookingAndBlIds(quoteId);
            if (null != bookingAndBlIds) {
                if (CommonFunctions.isNotNull(bookingAndBlIds[0])) {
                    bookingId = bookingAndBlIds[0].toString();
                } else {
                    bookingId = request.getParameter("bookingId");
                }
                if (CommonFunctions.isNotNull(bookingAndBlIds[1])) {
                    blId = bookingAndBlIds[1].toString();
                } else {
                    blId = request.getParameter("blId");
                }
            } else {
                bookingId = request.getParameter("bookingId");
                blId = request.getParameter("blId");
            }
        }
        for (ItemsForRoleBean tabItem : itemsForRole) {
            String program = tabItem.getProgram();
            if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.QUOTES_EXPORT)
                    && CommonUtils.isNotEmpty(quoteId) && !StringUtils.contains(quoteId, "null")) {
                if (CommonUtils.isNotEqual(quoteId, "0")) {
                    program = "/searchquotation.do?paramid1=" + quoteId;
                } else {
                    program = "/searchquotation.do?addQuote=addQuote";
                }
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                request.setAttribute("selectedTab", tabIndex);
                session.removeAttribute(ImportBc.sessionName);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.BOOKINGS_EXPORT)
                    && CommonUtils.isNotEmpty(bookingId) && !StringUtils.contains(bookingId, "null")) {
                if (CommonUtils.isNotEqual(bookingId, "0")) {
                    program = "/searchBookings.do?paramid=" + bookingId;
                } else {
                    program = "/searchBookings.do?newbooking=newbooking";
                }
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                request.setAttribute("selectedTab", tabIndex);
                session.removeAttribute(ImportBc.sessionName);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.FCL_BL_EXPORT)
                    && CommonUtils.isNotEmpty(blId) && !StringUtils.contains(blId, "null")) {
                if (CommonUtils.isNotEqual(blId, "0")) {
                    program = "/fclBlNew.do?methodName=goToHome&blId=" + blId;
                } else {
                    program = "/jsps/FCL/FclBl.jsp";
                }
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                request.setAttribute("selectedTab", tabIndex);
                tabIndex++;
                session.removeAttribute(ImportBc.sessionName);
            }

            if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.QUOTES_IMPORT)
                    && CommonUtils.isNotEmpty(quoteId) && !StringUtils.contains(quoteId, "null")) {
                if (CommonUtils.isNotEqual(quoteId, "0")) {
                    program = "/searchquotation.do?paramid1=" + quoteId;
                } else {
                    program = "/searchquotation.do?addQuote=addQuote";
                }
                program += "&date=" + new Date();
                tabItem.setSrc(request.getContextPath() + program);
                tabs.put(tabIndex + 1, tabItem);
                request.setAttribute("selectedTab", tabIndex);
                session.setAttribute(ImportBc.sessionName, ImportBc.sessionValue);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.BOOKINGS_IMPORT)
                    && CommonUtils.isNotEmpty(bookingId) && !StringUtils.contains(bookingId, "null")) {
                if (CommonUtils.isNotEqual(bookingId, "0")) {
                    program = "/searchBookings.do?paramid=" + bookingId;
                } else {
                    program = "/searchBookings.do?newbooking=newbooking";
                }
                program += "&date=" + new Date();
                tabItem.setSrc(request.getContextPath() + program);
                tabs.put(tabIndex + 1, tabItem);
                RefTerminal terminal = loginUser.getImportTerminal();
                if (null != terminal && CommonUtils.isNotEmpty(terminal.getTerminalLocation())) {
                    request.getSession().setAttribute("userIssuingTerminal", terminal.getTerminalLocation() + "-" + terminal.getTrmnum());
                }
                request.setAttribute("selectedTab", tabIndex);
                session.setAttribute(ImportBc.sessionName, ImportBc.sessionValue);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.FCL_BL_IMPORT)
                    && CommonUtils.isNotEmpty(blId) && !StringUtils.contains(blId, "null")) {
                if (CommonUtils.isNotEqual(blId, "0")) {
                    program = "/fclBL.do?paramid=" + blId;
                } else {
                    program = "/fclBL.do?newFclBL=newFclBL";
                }
                program += "&date=" + new Date();
                tabItem.setSrc(request.getContextPath() + program);
                tabs.put(tabIndex + 1, tabItem);
                RefTerminal terminal = loginUser.getImportTerminal();
                if (null != terminal && CommonUtils.isNotEmpty(terminal.getTerminalLocation())) {
                    request.getSession().setAttribute("userIssuingTerminal", terminal.getTerminalLocation() + "-" + terminal.getTrmnum());
                }
                request.setAttribute("selectedTab", tabIndex);
                session.setAttribute(ImportBc.sessionName, ImportBc.sessionValue);
                tabIndex++;
            }
        }
        return tabs;
    }

    public ActionForward gotoTradingPartner(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        setMenusFromSession(request.getSession(), homeForm);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        ItemDAO itemDAO = new ItemDAO();
        homeForm.setSelectedMenu("Accounting");
        Item parent = itemDAO.getItem("itemDesc", "Trading Partner Maintenance");
        homeForm.setItemId(parent.getItemId());
        Item child = itemDAO.getItem("itemDesc", "Trading Partner");
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        ItemsForRoleBean tab = new ItemsForRoleBean();
        tab.setItemId(child.getItemId());
        tab.setItemDesc(child.getItemDesc());
        StringBuilder src = new StringBuilder(request.getContextPath());
        src.append("/tradingPartner.do?buttonValue=gotoTradingPartner");
        src.append("&accountNumber=").append(request.getParameter("accountNumber"));
        src.append("&selectedTab=").append(request.getParameter("selectedTab").trim());
        src.append("&date=").append(new Date());
        tab.setSrc(src.toString());
        tabs.put(1, tab);
        homeForm.setTabs(tabs);
        return mapping.findForward("success");
    }

    private void setMenusFromSession(HttpSession session, HomeForm homeForm) {
        if (null != session.getAttribute("menus")) {
            homeForm.setMenus((TreeMap<String, LinkedHashMap<Object, Object>>) session.getAttribute("menus"));
        }
    }

    public ActionForward showServerStatus(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        Runtime runtime = Runtime.getRuntime();
        request.setAttribute("maxMemory", runtime.maxMemory() / 1048576);
        request.setAttribute("totalMemory", runtime.totalMemory() / 1048576);
        request.setAttribute("usedMemory", (runtime.totalMemory() - runtime.freeMemory()) / 1048576);
        request.setAttribute("freeMemory", runtime.freeMemory() / 1048576);
        request.setAttribute("freePercentage", (runtime.freeMemory() * 100) / runtime.totalMemory());
        return mapping.findForward(SERVER_STATUS);
    }

    public ActionForward showFollowUpTasks(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setLoginName(loginUser.getLoginName());
        return mapping.findForward(FOLLOW_UP_TASKS);
    }

    public ActionForward removeFollowUpTask(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        Notes followUpTask = new NotesDAO().findById(homeForm.getFollowUpTaskId());
        followUpTask.setRemoved(true);
        return showFollowUpTasks(mapping, form, request, response);
    }

    public ActionForward gotoArInquiry(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        setMenusFromSession(request.getSession(), homeForm);
        Integer roleId = null;
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        homeForm.setSelectedMenu("Accounting");
        Item parent = new ItemDAO().getItem("itemDesc", "Accounts Receivable");
        homeForm.setItemId(parent.getItemId());
        homeForm.setTabs(getTabs(homeForm, request, roleId));
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        for (Integer key : homeForm.getTabs().keySet()) {
            ItemsForRoleBean tab = homeForm.getTabs().get(key);
            if (CommonUtils.isEqual(tab.getItemDesc(), "AR Inquiry")) {
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append("/arInquiry.do?action=search");
                if (CommonUtils.isNotEmpty(homeForm.getToScreenName()) && "ar_inquiry".equalsIgnoreCase(homeForm.getToScreenName())) {
                    src.append("&toggled=true&selectedPage=1&sortBy=invoice_date&orderBy=asc&searchFilter=Dock Receipt");
                    src.append("&searchValue2=").append(homeForm.getFileNumber());
                    homeForm.setToScreenName(null);
                    homeForm.setFromScreen(null);
                } else {
                    src.append("&customerNumber=").append(homeForm.getCustomerNumber());
                    src.append("&customerName=").append(new TradingPartnerDAO().getAccountName(homeForm.getCustomerNumber()));
                }
                src.append("&date=").append(new Date());
                tab.setSrc(src.toString());
            }
            tabs.put(key, tab);
        }
        homeForm.setTabs(tabs);
        return mapping.findForward("success");
    }

    public ActionForward jump(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        setMenusFromSession(request.getSession(), homeForm);
        Integer roleId = null;
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        HttpSession session = request.getSession();
        if (CommonUtils.in(homeForm.getModule(), "AR INQUIRY", "AP INQUIRY", "ACCOUNT PAYABLE", "AR BATCH")) {
            homeForm.setSelectedMenu("Accounting");
            String itemDesc = CommonUtils.in(homeForm.getModule(), "AR INQUIRY", "AR BATCH") ? "Accounts Receivable" : "Accounts Payable";
            Item parent = new ItemDAO().getItem("itemDesc", itemDesc);
            homeForm.setItemId(parent.getItemId());
            TreeMap<Integer, ItemsForRoleBean> tabs = getTabs(homeForm, request, roleId);
            int selectedTab = 0;
            for (Integer key : tabs.keySet()) {
                ItemsForRoleBean tab = tabs.get(key);
                if (CommonUtils.isEqualIgnoreCase(tab.getItemDesc(), homeForm.getModule())) {
                    request.setAttribute("selectedTab", selectedTab);
                    StringBuilder src = new StringBuilder();
                    src.append(tab.getSrc());
                    src.append("&action=search");
                    if (CommonUtils.in(tab.getItemDesc(), "AP INQUIRY", "ACCOUNT PAYABLE")) {
                        src.append("&only=");
                        src.append("&vendorNumber=").append(homeForm.getReference());
                        src.append("&vendorName=").append(new TradingPartnerDAO().getAccountName(homeForm.getReference()));
                    } else if (CommonUtils.isEqualIgnoreCase(tab.getItemDesc(), "AR INQUIRY")) {
                        src.append("&customerNumber=").append(homeForm.getReference());
                        src.append("&customerName=").append(new TradingPartnerDAO().getAccountName(homeForm.getReference()));
                    }
                    tab.setSrc(src.toString());
                }
                selectedTab++;
            }
            homeForm.setTabs(tabs);
            session.setAttribute("followupFlag", Boolean.FALSE);
        } else if (CommonUtils.isEqualIgnoreCase(homeForm.getModule(), "FCL FILE")) {
            String accessMode = homeForm.getAccessMode();
            String lockMessage = "view".equalsIgnoreCase(accessMode) ? "" : new com.logiware.fcl.dao.SearchDAO().checkLocking(homeForm.getReference(), loginUser.getUserId());
            if (CommonUtils.isNotEmpty(lockMessage)) {
                request.setAttribute("lockMessage", lockMessage);
            } else {
                String fileNumber = homeForm.getReference();
                boolean isImportFile = new FclDAO().isImportFile(fileNumber);
                homeForm.setSelectedMenu(isImportFile ? "Imports" : "Exports");
                ItemDAO itemDAO = new ItemDAO();
                if (isImportFile) {
                    homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes, Bookings, and BLs", "FCLIMP")));
                    session.setAttribute(ImportBc.sessionName, ImportBc.sessionName);
                } else {
                    homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes, Bookings, and BLs", "")));
                    session.removeAttribute(ImportBc.sessionName);
                    String companyCode = new SystemRulesDAO().getSystemRules("CompanyCode");
                    session.setAttribute("companyMnemonicCode", new SystemRulesDAO().getSystemRules("CompanyNameMnemonic"));
                    if ("02".equals(companyCode)) {
                        session.setAttribute("companyCode", "OTIC");
                    } else {
                        session.setAttribute("companyCode", "ECCI");
                    }
                }
                TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
                List<ItemsForRoleBean> tabItems = new ItemDAO().getFclOpsScreens(homeForm.getItemId(), roleId, fileNumber, isImportFile, request.getContextPath());
                int tabIndex = 0;
                for (ItemsForRoleBean tabItem : tabItems) {
                    tabs.put(tabIndex + 1, tabItem);
                    tabIndex++;
                }
                homeForm.setRecordId(fileNumber);
                homeForm.setTabs(tabs);
                request.setAttribute("selectedTab", tabIndex - 1);
                request.setAttribute("fileNumber", fileNumber);
                session.setAttribute("followupFlag", Boolean.FALSE);
            }
        } else if (CommonUtils.isEqualIgnoreCase(homeForm.getModule(), "LCL FILE")) {
            String accessMode = homeForm.getAccessMode();
            String lockMessage = "view".equalsIgnoreCase(accessMode) ? "" : new SearchDAO().checkLocking(homeForm.getReference(), loginUser.getUserId());
            if (CommonUtils.isNotEmpty(lockMessage)) {
                request.setAttribute("lockMessage", lockMessage);
            } else {
                String fileNumber = homeForm.getReference();
                LclFileNumberDAO lclFileNumberDAO = new LclFileNumberDAO();
                boolean isImportFile = lclFileNumberDAO.isImportFile(fileNumber);
                homeForm.setSelectedMenu(isImportFile ? "Imports" : "Exports");
                LclSession lclSession = null != session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
                lclSession.setSelectedMenu(homeForm.getSelectedMenu());
                session.setAttribute("lclSession", lclSession);
                ItemDAO itemDAO = new ItemDAO();
                if (isImportFile) {
                    homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes/Dock Receipts", "LCLQB")));
                    String companyMnemonicCode = LoadLogisoftProperties.getProperty("application.fclBl.print.companyName");
                    String applicationEmailCompanyName = LoadLogisoftProperties.getProperty("application.email.companyName");
                    session.setAttribute("companyMnemonicCode", null != companyMnemonicCode ? companyMnemonicCode.toUpperCase() : "");
                    session.setAttribute("applicationEmailCompanyName", null != applicationEmailCompanyName ? applicationEmailCompanyName.toUpperCase() : "");
                } else {
                    homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes, Bookings AND BLs", "LCLQBBL")));
                    String companyMnemonicCode = LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic");
                    session.setAttribute("companyMnemonicCode", null != companyMnemonicCode ? companyMnemonicCode.toUpperCase() : "");
                }
                TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
                List<ItemsForRoleBean> tabItems = new ItemDAO().getLclOpsScreens(homeForm.getItemId(), roleId, fileNumber, isImportFile, request.getContextPath());
                int tabIndex = 0;
                for (ItemsForRoleBean tabItem : tabItems) {
                    tabs.put(tabIndex + 1, tabItem);
                    tabIndex++;
                }
                homeForm.setRecordId(fileNumber);
                homeForm.setTabs(tabs);
                Long fileNumberId = lclFileNumberDAO.getFileIdByFileNumber(fileNumber);
                request.setAttribute("selectedTab", tabIndex - 1);
                request.setAttribute("fileNumber", fileNumber);
                request.setAttribute("fileNumberId", String.valueOf(fileNumberId));
                session.setAttribute("followupFlag", Boolean.FALSE);
            }
        } else if (CommonUtils.isEqualIgnoreCase(homeForm.getModule(), "LCL VOYAGE")) {
            String voyageNo = homeForm.getReference();
            boolean isImportVoyage = new LclSsHeaderDAO().isImportVoyage(voyageNo);
            homeForm.setSelectedMenu(isImportVoyage ? "Imports" : "Exports");
            LclSession lclSession = null != session.getAttribute("lclSession") ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setSelectedMenu(homeForm.getSelectedMenu());
            session.setAttribute("lclSession", lclSession);
            ItemDAO itemDAO = new ItemDAO();
            if (isImportVoyage) {
                homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Imports Voyages/Units", "LCLIVU")));
            } else {
                homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Units and Voyages", "LCLUS")));
            }
            TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
            List<ItemsForRoleBean> tabItems = new ItemDAO().getLclVoyageScreens(homeForm.getItemId(), roleId, voyageNo, isImportVoyage, request.getContextPath());
            int tabIndex = 0;
            for (ItemsForRoleBean tabItem : tabItems) {
                tabs.put(tabIndex + 1, tabItem);
                tabIndex++;
            }
            homeForm.setRecordId(voyageNo);
            homeForm.setTabs(tabs);
            request.setAttribute("selectedTab", 0);
            request.setAttribute("fileNumber", voyageNo);
            session.setAttribute("followupFlag", Boolean.FALSE);
        } else {
            session.setAttribute("followupFlag", Boolean.FALSE);
        }
        return mapping.findForward("success");
    }

    private StringBuilder appendScanAttachQueryString(String id, String screenName,
            String moduleName, String operationType, StringBuilder buffer) {
        if (null != buffer) {
            buffer.append("&moduleId=").append(id);
            buffer.append("&screenName=").append(screenName);
            buffer.append("&operationType=").append(operationType);
        }
        return buffer;
    }

    public ActionForward gotoFclOpsScreens(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        Integer roleId = null;
        setMenusFromSession(request.getSession(), homeForm);
        HttpSession session = request.getSession(true);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        String fileNumber = request.getParameter("fileNumber");
        TreeMap<Integer, ItemsForRoleBean> screens = new TreeMap<Integer, ItemsForRoleBean>();
        if (CommonUtils.isNotEmpty(homeForm.getFclFileType()) && !"undefined".equals(homeForm.getFclFileType())) {
            this.fclSetItemId(homeForm, session);
        }
        boolean isImport = (null != request.getSession().getAttribute(ImportBc.sessionName));
        List<ItemsForRoleBean> screensForMenu = new ItemDAO().getFclOpsScreens(homeForm.getItemId(), roleId, fileNumber, isImport, request.getContextPath());
        int tabIndex = 0;
        for (ItemsForRoleBean tabItem : screensForMenu) {
            screens.put(tabIndex + 1, tabItem);
            tabIndex++;
        }
        homeForm.setRecordId(fileNumber);
        homeForm.setTabs(screens);
        request.setAttribute("selectedTab", tabIndex - 1);
        request.setAttribute("fileNumber", fileNumber);
        session.setAttribute("homeScreenFileFlag", homeScreenFileFlag);
        session.setAttribute("followupFlag", Boolean.FALSE);
        return mapping.findForward("success");
    }

    public ActionForward gotoDomestBookingScreens(ActionMapping mapping,
            ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        Integer roleId = null;
        setMenusFromSession(request.getSession(), homeForm);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        String bookingNumber = request.getParameter("bookingNumber");
        TreeMap<Integer, ItemsForRoleBean> screens = new TreeMap<Integer, ItemsForRoleBean>();
        TreeMap<Integer, ItemsForRoleBean> items = getTabs(homeForm, request, roleId);
        for (Map.Entry<Integer, ItemsForRoleBean> entry : items.entrySet()) {
            ItemsForRoleBean bean = entry.getValue();
            if (!"Customer Rate Quote".equalsIgnoreCase(bean.getItemDesc())) {
                bean.setProgram(bean.getProgram() + "&bookingNumber=" + bookingNumber);
                bean.setSrc(bean.getSrc() + "&bookingNumber=" + bookingNumber);
                screens.put(1, bean);
            } else {
                screens.put(0, bean);
            }
        }
        homeForm.setTabs(screens);
        request.setAttribute("selectedTab", 1);
        return mapping.findForward("success");
    }

    public ActionForward gotoFclSearchScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        HttpSession session = request.getSession();
        User loginUser = (User) session.getAttribute("loginuser");
        Integer SSMasterItemId = (Integer) session.getAttribute("SSMasterItemId");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        Integer roleId = null;
        setMenusFromSession(session, homeForm);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        TreeMap<Integer, ItemsForRoleBean> screens = new TreeMap<Integer, ItemsForRoleBean>();
        String screenName = (null != session.getAttribute(ImportBc.sessionName)) ? "'IMPORT FILE SEARCH'" : "'FCL Search'";
        if (CommonUtils.isNotEmpty(SSMasterItemId)) {
            homeForm.setItemId(SSMasterItemId);
            screenName = "'SS MASTER DISPUTED BL'";
        }
        List<ItemsForRoleBean> screensForMenu = new ItemDAO().getScreensForMenu(homeForm.getItemId(), roleId, screenName);
        int tabIndex = 0;
        for (ItemsForRoleBean tabItem : screensForMenu) {
            tabItem.setSrc(request.getContextPath() + tabItem.getSrc().replace("=reset", "=goBack"));
            screens.put(tabIndex + 1, tabItem);
            tabIndex++;
        }
        homeForm.setTabs(screens);
        session.removeAttribute("SSMasterItemId");
        request.setAttribute("selectedTab", tabIndex - 1);
        return mapping.findForward("success");
    }

    public ActionForward gotoNewFclScreen(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        User loginUser = (User) request.getSession().getAttribute("loginuser");
        homeForm.setUserName(loginUser.getFirstName() + " " + loginUser.getLastName());
        Integer roleId = null;
        setMenusFromSession(request.getSession(), homeForm);
        if (null != loginUser.getTerminal()) {
            homeForm.setTerminalInfo(loginUser.getTerminal().getTrmnum() + " - " + loginUser.getTerminal().getTerminalLocation());
        }
        if (null != loginUser.getRole() && CommonUtils.isNotEmpty(loginUser.getRole().getRoleDesc())) {
            roleId = loginUser.getRole().getRoleId();
            homeForm.setRoleName(loginUser.getRole().getRoleDesc());
        }
        RefTerminal terminal = loginUser.getImportTerminal();
        if (null != terminal && CommonUtils.isNotEmpty(terminal.getTerminalLocation())) {
            request.getSession().setAttribute("userIssuingTerminal", terminal.getTerminalLocation() + "-" + terminal.getTrmnum());
        }
        TreeMap<Integer, ItemsForRoleBean> screens = new TreeMap<Integer, ItemsForRoleBean>();
        String screenName = homeForm.getTabName();
        List<ItemsForRoleBean> screensForMenu = new ItemDAO().getNewFclScreen(homeForm.getItemId(), roleId, screenName, request.getContextPath());
        int tabIndex = 0;
        for (ItemsForRoleBean tabItem : screensForMenu) {
            screens.put(tabIndex + 1, tabItem);
            tabIndex++;
        }
        homeForm.setTabs(screens);
        request.setAttribute("selectedTab", tabIndex - 1);
        return mapping.findForward("success");
    }

    @Override
    protected ActionForward unspecified(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        return showHome(mapping, form, request, response);
    }

    private TreeMap<Integer, ItemsForRoleBean> getTabsForLclExportVoyage(HomeForm homeForm,
            HttpServletRequest request, Integer roleId) throws Exception {
        TreeMap<Integer, ItemsForRoleBean> tabs = new TreeMap<Integer, ItemsForRoleBean>();
        List<ItemsForRoleBean> itemsForRole = new ItemDAO().getItemsForRole(homeForm.getItemId(), roleId);
        HttpSession session = request.getSession();
        int tabIndex = 0;
        String fileNumber = "";
        Long fileNumberId = 0l;
        String moduleId = "";
        if (null != request.getParameter("recordId") && !request.getParameter("recordId").equals("")) {
            fileNumber = new LclFileNumberDAO().getFileNumberByFileId(request.getParameter("recordId"));
            fileNumberId = Long.parseLong(request.getParameter("recordId"));
            moduleId = new LclFileNumberDAO().getFileStateById(fileNumberId.toString());
        } else {
            moduleId = request.getParameter("moduleId");
        }
        if (CommonUtils.isEmpty(fileNumber)) {
            LclSession lclSession = (LclSession) session.getAttribute("lclSession") != null ? (LclSession) session.getAttribute("lclSession") : new LclSession();
            lclSession.setCommodityList(null);
            lclSession.setQuoteCommodityList(null);
            lclSession.setQuoteAcList(null);
        }
        session.setAttribute("companyMnemonicCode", null != LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic")
                ? LoadLogisoftProperties.getProperty("application.fclBl.edi.company.mnemonic").toUpperCase() : "");
        LclQuote lclQuote = null;
        LclBl lclBl = null;
        LclBooking lclBooking = null;
        LCLQuoteDAO lclQuoteDAO = new LCLQuoteDAO();
        LCLBlDAO lclBlDAO = new LCLBlDAO();
        LCLBookingDAO lclBookingDAO = new LCLBookingDAO();
        int tabIndexQuote = 0;
        int tabIndexBkg = 0;
        int tabIndexBl = 0;
        if (CommonUtils.isNotEmpty(fileNumberId)) {
            lclQuote = lclQuoteDAO.findById(fileNumberId);
            lclBl = lclBlDAO.findById(fileNumberId);
            lclBooking = lclBookingDAO.findById(fileNumberId);
            /* consolidation check */
            if (null == lclBl) {
                Long consolidateBlId = lclBlDAO.findConsolidateBl(fileNumberId);
                if (null != consolidateBlId) {
                    lclBl = lclBlDAO.findById(consolidateBlId);
                }
            }
            /* ends */
            if (lclQuote != null && lclBl != null && lclBooking != null) {
                tabIndexQuote = 0;
                tabIndexBkg = 2;
                tabIndexBl = 1;
            } else if (lclBl != null && lclBooking != null) {
                if (LclCommonConstant.LCL_EXPORT.equalsIgnoreCase(homeForm.getSelectedMenu())) {
                    tabIndexBkg = 1;
                    tabIndexBl = 0;
                    if ("converToBlFlag".equalsIgnoreCase(homeForm.getCallBack())) {
                        tabIndexBkg = 0;
                        tabIndexBl = 1;
                    }
                } else {
                    tabIndexBkg = 0;
                }
            } else if (lclBooking != null && lclQuote != null) {
                tabIndexQuote = 0;
                tabIndexBkg = 1;
            } else if (lclQuote != null) {
                tabIndexQuote = 0;
            } else if (lclBooking != null) {
                tabIndexBkg = 0;
            }
        }
        for (ItemsForRoleBean tabItem : itemsForRole) {
            String program = tabItem.getProgram();
            if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.QUOTES_EXPORT)) {
                if (moduleId.equals("Quotes") || moduleId.equals("Q") || moduleId.equals("B") || moduleId.equals("BL")) {
                    if ((moduleId.equals("B") || moduleId.equals("BL")) && lclQuote == null) {
                    } else {
                        if (CommonUtils.isNotEmpty(fileNumberId) && lclQuote != null) {
                            program = "/lclQuote.do?methodName=editQuote&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
                                    + "&moduleName=" + homeForm.getSelectedMenu() + "&callBack=" + request.getParameter("callBack")
                                    + "&toScreenName=" + homeForm.getToScreenName() + "&fromScreen=" + homeForm.getFromScreen()
                                    + "&pickVoyId=" + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId() + "&filterByChanges=" + homeForm.getFilterByChanges()
                                    + "&unitSsId=" + homeForm.getUnitSsId() + "&inTransitDr=" + homeForm.getInTransitDr();
                        } else {
                            program = "/lclQuote.do?methodName=newQuote" + "&moduleName=" + homeForm.getSelectedMenu();
                        }
                        String queryString = "?";
                        if (program.contains("?")) {
                            queryString = "&";
                        }
                        if (program.endsWith("?")) {
                            queryString = "";
                        }
                        if (!program.startsWith("/")) {
                            program = "/";
                        }
                        Integer accessMode = 0;
                        if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                            accessMode = 1;
                        }
                        StringBuilder src = new StringBuilder(request.getContextPath());
                        src.append(program).append(queryString);
                        src.append("modify=").append(accessMode);
                        src.append("&programid=").append(tabItem.getItemId());
                        src.append("&accessMode=").append(accessMode);
                        src.append("&date=").append(new Date());
                        appendScanAttachQueryString(fileNumber, "LCL FILE", "Quote", "Scan or Attach", src);
                        tabItem.setSrc(src.toString());
                        tabs.put(tabIndex + 1, tabItem);
                        request.setAttribute("selectedTab", tabIndexQuote);
                        tabIndex++;
                    }
                }
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.BOOKINGS_EXPORT)) {
                if (moduleId.equals("Bookings") || moduleId.equals("B") || moduleId.equals("BL")) {
                    if (CommonUtils.isNotEmpty(fileNumberId)) {
                        program = "/lclBooking.do?methodName=editBooking&fileNumberId=" + fileNumberId + "&fileNumber=" + fileNumber
                                + "&moduleName=" + homeForm.getSelectedMenu() + "&callBack=" + request.getParameter("callBack")
                                + "&toScreenName=" + homeForm.getToScreenName() + "&fromScreen=" + homeForm.getFromScreen()
                                + "&pickVoyId=" + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId() + "&filterByChanges=" + homeForm.getFilterByChanges()
                                + "&unitSsId=" + homeForm.getUnitSsId() + "&inTransitDr=" + homeForm.getInTransitDr();
                    } else {
                        String shortShipFileNo = request.getParameter("shortShipfileNo");
                        if (CommonUtils.isNotEmpty(shortShipFileNo)) {
                            program = "/lclBooking.do?methodName=createShortShip&moduleName=" + homeForm.getSelectedMenu()
                                    + "&shortShip=" + shortShipFileNo + "&pickVoyId=" + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId()
                                    + "&filterByChanges=" + homeForm.getFilterByChanges() + "&unitSsId=" + homeForm.getUnitSsId()
                                    + "&toScreenName=" + homeForm.getToScreenName() + "&fromScreen=" + homeForm.getFromScreen() + "&inTransitDr=false";
                        } else {
                            program = "/lclBooking.do?methodName=newBooking" + "&moduleName=" + homeForm.getSelectedMenu();
                        }
                    }
                    String queryString = "?";
                    if (program.contains("?")) {
                        queryString = "&";
                    }
                    if (program.endsWith("?")) {
                        queryString = "";
                    }
                    if (!program.startsWith("/")) {
                        program = "/";
                    }
                    Integer accessMode = 0;
                    if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                        accessMode = 1;
                    }
                    StringBuilder src = new StringBuilder(request.getContextPath());
                    src.append(program).append(queryString);
                    src.append("modify=").append(accessMode);
                    src.append("&programid=").append(tabItem.getItemId());
                    src.append("&accessMode=").append(accessMode);
                    src.append("&date=").append(new Date());
                    appendScanAttachQueryString(fileNumber, "LCL FILE", "Booking", "Scan or Attach", src);
                    tabItem.setSrc(src.toString());
                    tabs.put(tabIndex + 1, tabItem);
                    request.setAttribute("selectedTab", tabIndexBkg);
                    tabIndex++;
                }
            } else if (lclBl != null && CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_BL_EXPORT)
                    && LclCommonConstant.LCL_EXPORT.equalsIgnoreCase(homeForm.getSelectedMenu()) && moduleId.equals("BL")) {
                if (CommonUtils.isNotEmpty(fileNumberId)) {
                    program = "/lclBl.do?methodName=editBooking&fileNumberId=" + lclBl.getLclFileNumber().getId() + "&fileNumber=" + lclBl.getLclFileNumber().getFileNumber()
                            + "&moduleName=" + homeForm.getSelectedMenu() + "&callBack=" + request.getParameter("callBack")
                            + "&toScreenName=" + homeForm.getToScreenName() + "&fromScreen=" + homeForm.getFromScreen()
                            + "&pickVoyId=" + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId() + "&filterByChanges=" + homeForm.getFilterByChanges()
                            + "&unitSsId=" + homeForm.getUnitSsId() + "&inTransitDr=" + homeForm.getInTransitDr()
                            + "&bookingFileNumberId=" + fileNumberId;
                } else {
                    program = "/lclBl.do?methodName=newBooking";
                }
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                src.append("&moduleName=").append(homeForm.getSelectedMenu());
                appendScanAttachQueryString(fileNumber, "LCL FILE", "Bl", "Scan or Attach", src);
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                request.setAttribute("selectedTab", tabIndexBl);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SCHEDULE_SEARCH_EXPORT)) {
                program = "/lclAddVoyage.do?methodName=editVoyage&headerId=" + homeForm.getPickVoyId() + "&detailId=" + homeForm.getDetailId()
                        + "&filterByChanges=" + homeForm.getFilterByChanges() + "&unitssId=" + homeForm.getUnitSsId() + "&toScreenName=" + homeForm.getFromScreen()
                        + "&fromScreen=" + homeForm.getToScreenName() + "&intransitDr=" + homeForm.getInTransitDr() + "&releaseLock=true";
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                src.append("&moduleName=").append(homeForm.getSelectedMenu());
                appendScanAttachQueryString(fileNumber, "LCL FILE", "Bl", "Scan or Attach", src);
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex, tabItem);
                request.setAttribute("selectedTab", tabIndex);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), ItemConstants.LCL_SCHEDULE_EXPORT_VOYAGE_NOTIFICATION)) {
                program = tabItem.getProgram();
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                src.append("&moduleName=").append(homeForm.getSelectedMenu());
                appendScanAttachQueryString(fileNumber, "LCL FILE", "Bl", "Scan or Attach", src);
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                tabIndex++;
            } else if (CommonUtils.isEqualIgnoreCase(tabItem.getItemDesc(), "SS MASTER DISPUTED BL")) {
                program = tabItem.getProgram() + "&fromScreen=EXP_VOYAGE";
                String queryString = "?";
                if (program.contains("?")) {
                    queryString = "&";
                }
                if (program.endsWith("?")) {
                    queryString = "";
                }
                if (!program.startsWith("/")) {
                    program = "/";
                }
                Integer accessMode = 0;
                if (CommonUtils.isNotEmpty(tabItem.getModify())) {
                    accessMode = 1;
                }
                StringBuilder src = new StringBuilder(request.getContextPath());
                src.append(program).append(queryString);
                src.append("modify=").append(accessMode);
                src.append("&programid=").append(tabItem.getItemId());
                src.append("&accessMode=").append(accessMode);
                src.append("&date=").append(new Date());
                src.append("&moduleName=").append(homeForm.getSelectedMenu());
                tabItem.setSrc(src.toString());
                tabs.put(tabIndex + 1, tabItem);
                tabIndex++;
            }
        }
        return tabs;
    }

    public ActionForward checkHomeFileLock(ActionMapping mapping, ActionForm form,
            HttpServletRequest request, HttpServletResponse response) throws Exception {
        HomeForm homeForm = (HomeForm) form;
        SearchDAO searchDAO = new SearchDAO();
        searchDAO.checkLockingFile(homeForm.getFileNumber(), homeForm.getUserId(), homeForm.getScreenName(), response);
        return null;
    }

    private HomeForm lclSetItemId(HomeForm homeForm, HttpServletRequest request) {
        ItemDAO itemDAO = new ItemDAO();
        if (CommonUtils.isNotEmpty(homeForm.getLclFileType()) && !"undefined".equals(homeForm.getLclFileType())) {
            if (homeForm.getLclFileType().equalsIgnoreCase("I")) {
                homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes/Dock Receipts", "LCLQB")));
                this.homeScreenFileFlag = Boolean.TRUE;
            } else if (homeForm.getLclFileType().equalsIgnoreCase("E")) {
                homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes, Bookings AND BLs", "LCLQBBL")));
                this.homeScreenFileFlag = Boolean.TRUE;
            }
        } else if (CommonUtils.isEmpty(homeForm.getItemId()) && "OPEN IMP VOYAGE".equalsIgnoreCase(request.getParameter("callBack"))) {
            homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Imports Voyages/Units", "LCLIVU")));
            this.homeScreenFileFlag = Boolean.TRUE;
        } else if (null != homeForm.getCallBack() && "LCLIMPDDP".equalsIgnoreCase(homeForm.getCallBack())) {
            homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes/Dock Receipts", "LCLQB")));
            homeForm.setTabName("doorDeliverySearchTab");
        } else if (null != homeForm.getCallBack() && "doorDeliverySearchTab".equalsIgnoreCase(homeForm.getCallBack())) {
            homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Door Delivery Pool", "LCLIDDP")));
        } else {
            this.homeScreenFileFlag = Boolean.FALSE;
        }
        return homeForm;
    }

    private HomeForm fclSetItemId(HomeForm homeForm, HttpSession session) {
        ItemDAO itemDAO = new ItemDAO();
        if (homeForm.getFclFileType().equalsIgnoreCase("I")) {
            homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes, Bookings, and BLs", "FCLIMP")));
            this.homeScreenFileFlag = Boolean.TRUE;
            session.setAttribute(ImportBc.sessionName, ImportBc.sessionName);
        } else if (CommonUtils.in(homeForm.getFclFileType(), "S", "P", "C")) {
            homeForm.setItemId(Integer.parseInt(itemDAO.getItemId("Quotes, Bookings, and BLs", "")));
            this.homeScreenFileFlag = Boolean.TRUE;
            session.removeAttribute(ImportBc.sessionName);
        } else {
            this.homeScreenFileFlag = Boolean.FALSE;

        }
        return homeForm;
    }
}
