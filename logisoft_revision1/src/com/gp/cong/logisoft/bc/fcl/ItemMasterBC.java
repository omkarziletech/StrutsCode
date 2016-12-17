package com.gp.cong.logisoft.bc.fcl;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.util.CommonFunctions;

public class ItemMasterBC {

    public static String BOOKING_QUOTE_BL_IMPORT = "Quotes, Bookings, and BLs";
    public static String BOOKING_QUOTE_BL_EXPORT = "Quotes, Bookings, and BLs";
    //------------------------------------------
    public static String FILESEARCH = "IMPORT FILE SEARCH";
    public static String QUOTE = "IMPORT QUOTE";
    public static String BOOKING = "IMPORT BOOKING";
    public static String FCLBL = "IMPORT FCL BL";
    public static String DELIMETER = "=";

    public void getImportPage(String folderId, HttpSession session) throws Exception {
        ItemDAO itemDAO = new ItemDAO();
        Item item = itemDAO.findById(new Integer(folderId));
        User user = CommonFunctions.isNotNull(session.getAttribute("loginuser")) ? (User) session.getAttribute("loginuser") : null;
        if (user != null) {
            String sessionValue = null;
            if (null != item && null != item.getItemDesc() && item.getItemDesc().equalsIgnoreCase(BOOKING_QUOTE_BL_IMPORT)) {
                sessionValue = ImportBc.sessionValue + DELIMETER + user.getId();
                if (null == session.getAttribute(ImportBc.sessionName)) {
                    if (null != session.getAttribute(QuotationConstants.FILESEARCHLIST)) {
                        session.removeAttribute(QuotationConstants.FILESEARCHLIST);
                    }
                }
                session.setAttribute(ImportBc.sessionName, sessionValue);
                //need to test

            } else if (null != item && null != item.getItemDesc() && item.getItemDesc().equalsIgnoreCase(BOOKING_QUOTE_BL_EXPORT)
                    && CommonFunctions.isNotNull(session.getAttribute(ImportBc.sessionName))) {
                sessionValue = (String) session.getAttribute(ImportBc.sessionName);
                String arg[] = sessionValue.split(DELIMETER);
                if (null != arg && arg.length > 0) {
                    Integer id = new Integer(arg[1]);
                    if (user.getId().equals(id)) {
                        if (null != session.getAttribute(ImportBc.sessionName)) {
                            session.removeAttribute(ImportBc.sessionName);
                            if (null != session.getAttribute(QuotationConstants.FILESEARCHLIST)) {
                                session.removeAttribute(QuotationConstants.FILESEARCHLIST);
                            }
                        }
                    }
                }
                // need to test


            }
        }

    }

    public String findFolderKey(String itemDesc) throws Exception {
        String id = null;
        ItemDAO itemDAO = new ItemDAO();
        List list = itemDAO.findItemName(itemDesc);
        if (CommonFunctions.isNotNullOrNotEmpty(list)) {
            Item item = (Item) list.get(0);
            id = item.getItemId().toString();
        }
        return id;
    }
}
