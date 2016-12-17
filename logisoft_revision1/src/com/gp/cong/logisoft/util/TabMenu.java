package com.gp.cong.logisoft.util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gp.cong.logisoft.bc.fcl.ItemMasterBC;
import com.gp.cong.logisoft.bc.scheduler.ProcessInfoBC;
import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ItemTree;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RoleItemAssociation;
import com.gp.cong.logisoft.domain.User;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemTreeDAO;
import com.gp.cong.logisoft.hibernate.dao.RoleDAO;

public class TabMenu extends BaseHibernateDAO {

    ItemTreeDAO itemTreeDAO = new ItemTreeDAO();
    List treeList = new ArrayList();

    public List getTabStucture(HttpServletRequest request, Integer itemId, User user, String tabName)throws Exception {
    	Integer roleId=0;
    	Integer userId=0;
    	if(null!=user){
    		roleId=(null!=user.getRole())?user.getRole().getRoleId():0;
    		userId=user.getUserId();
    	}
            ItemDAO itemDAO = new ItemDAO();
            Item item1 = itemDAO.findById(itemId);
            List itemTreeList = itemTreeDAO.getChild(item1);
            for (int j = 0; j < itemTreeList.size(); j++) {
                ItemTree itTree = (ItemTree) itemTreeList.get(j);
                Item it = itemDAO.findById(itTree.getItemId());
                RoleDAO roleDAO = new RoleDAO();
                Role role1 = roleDAO.findById(roleId);
                Set roleTreeStructure = role1.getRoleItem();
                Iterator iter = roleTreeStructure.iterator();
                while (iter.hasNext()) {
                    RoleItemAssociation roleTree = (RoleItemAssociation) iter.next();
                    Item item = roleTree.getItemId();
                    String modify = null;
                    String programid = null;
                    if (roleTree.getModify() != null) {
                        if (roleTree.getDeleted() != null) {
                            // Show everything
                            modify = "2";
                        } else {
                            // Show everything except delete
                            modify = "1";
                        }
                    } else {
                        modify = "0";
                    }
                    programid = it.getItemId().toString();
                    if (item.getItemId().equals(it.getItemId()) && it.getProgramName() != null && it.getProgramName() != "{null}") {
                    	
                    	if (item.getItemId().equals(56) || item.getItemId().equals(57) || item.getItemId().equals(58) || item.getItemId().equals(198)) {
                        	// this block wil excute when we hit from export
                    		if (tabName != null && !tabName.equalsIgnoreCase("")) {
                                if (tabName.equals("fileSearch") && item.getItemId().equals(56)) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                }else if (tabName.equals("quotes") && item.getItemId().equals(57)) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                } else if (tabName.equals("booking") && (item.getItemId().equals(58) || item.getItemId().equals(57))) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                } else if (tabName.equals("fclBl") && (item.getItemId().equals(58) || item.getItemId().equals(57) || item.getItemId().equals(198))) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                } else {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    if (item.getItemId().equals(56)) {
                                        if (tabName.equals("fileSearch")) {
                                            treeList.add(st);
                                        }
                                    } else {
                                        treeList.add(st);
                                    }
                                }
                            } else {
                                if (item.getItemId().equals(56)) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));
                                   //-----------------------Locking..........................
                                   // ProcessInfoBC processInfoBC = new ProcessInfoBC();
                                //   processInfoBC.deleteAllRecordsWhileChangeTab(userId);
                                    break;

                                }else {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));
                                    break;
                                }
                               
                            }
                            
                        }
                    	else if (null!=item.getItemDesc() && (item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.QUOTE) ||
                    			item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.BOOKING) ||
                    			item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.FILESEARCH) ||
                    			item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.FCLBL))) {
                    		// this block wil execute when we hit from quote booking,bl from import page
                        	if (tabName != null && !tabName.equalsIgnoreCase("")) {
                                if (tabName.equals("fileSearch") && item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.FILESEARCH)) {//56
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                }else if (tabName.equals("newBooking") && item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.BOOKING)) {//58
                                	 String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                     " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                     "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                     "modify=" + modify + "&programid=" + programid + "'   " +
                                     "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                     "</div>";
                             treeList.add(new String(st));

                                } else if (tabName.equals("newFclBl") && item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.FCLBL)) {//198
                               	 String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                 " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                 "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                 "modify=" + modify + "&programid=" + programid + "'   " +
                                 "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                 "</div>";
                            	 treeList.add(new String(st));

                            }else if (tabName.equals("quotes") && item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.QUOTE)) {//57
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                } else if (tabName.equals("booking") && (item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.BOOKING))) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                }else if(tabName.equals("booking") &&  CommonFunctions.isNotNull(request.getParameter("quoteid")) &&
                                		!"null".equals(request.getParameter("quoteid")) &&
                                		item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.QUOTE)){
                                	String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                    " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                    "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                    "modify=" + modify + "&programid=" + programid + "'   " +
                                    "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                    "</div>";
                                	treeList.add(new String(st));
                                } else if (tabName.equals("fclBl") && item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.FCLBL)) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));

                                }else if(tabName.equals("fclBl") &&  (CommonFunctions.isNotNull(request.getParameter("bokingid")) &&
                                		!"null".equals(request.getParameter("bokingid")) &&
                                		item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.BOOKING) || (CommonFunctions.isNotNull(request.getParameter("quoteid"))) &&
                                        		!"null".equals(request.getParameter("quoteid")) &&
                                        		item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.QUOTE))){
                                	String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                    " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                    "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                    "modify=" + modify + "&programid=" + programid + "'   " +
                                    "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                    "</div>";
                                	treeList.add(new String(st));
                                } else {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    if (item.getItemId().equals(56)) {
                                        if (tabName.equals("fileSearch")) {
                                            treeList.add(st);
                                        }
                                    } else {
                                        treeList.add(st);
                                    }
                                }
                            } else {
                                if ( item.getItemDesc().trim().equalsIgnoreCase(ItemMasterBC.FILESEARCH)) {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));
                                    //---------------------
                                    break;
                                }else {
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"\"  " +
                                            " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                            "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + "?" +
                                            "modify=" + modify + "&programid=" + programid + "'   " +
                                            "width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                            "</div>";
                                    treeList.add(new String(st));
                                    break;
                                }
                               
                            }
                            
                        
                    	}
                    	else {
                            if (item.getItemId().equals(it.getItemId()) && it.getProgramName() != null && it.getProgramName() != "{null}") {
                                    String queryString = "?";
                                    if(it.getProgramName().contains("?")){
                                        queryString = "&";
                                    }
                                    if(it.getProgramName().endsWith("?")){
                                        queryString = "";
                                    }
                                    String st = "<div id=tab" + it.getItemId() + "  dojoType=\"ContentPane\"  " +
                                        " style=\"font-family:Arial;display:none;\" label='" + it.getItemDesc().toUpperCase() + " '  >" +
                                        "<iframe onLoad='showTabs();' src='" + request.getContextPath() + it.getProgramName() + queryString +
                                        "modify=" + modify + "&programid=" + programid + "&accessMode="+modify+"'"+
                                        "   width=\"100%\" height=\"100%\" frameborder=\"0\" name='" + tabName + "' id='" + tabName + "'> </iframe>" +
                                        "</div>";

                                treeList.add(new String(st));
                                break;
                            }
                        }
                    }
                }
            }
        return treeList;
    }

    public List getUniqueCode(HttpServletRequest request, Integer itemId, Integer roleId) throws Exception {
        List itemList = new ArrayList();
            ItemDAO itemDAO = new ItemDAO();
            Item item1 = itemDAO.findById(itemId);
            List itemTreeList = itemTreeDAO.getChild(item1);
            for (int j = 0; j < itemTreeList.size(); j++) {
                ItemTree itTree = (ItemTree) itemTreeList.get(j);
                Item it = itemDAO.findById(itTree.getItemId());
                RoleDAO roleDAO = new RoleDAO();
                Role role1 = roleDAO.findById(roleId);
                Set roleTreeStructure = role1.getRoleItem();
                Iterator iter = roleTreeStructure.iterator();
                while (iter.hasNext()) {
                    RoleItemAssociation roleTree = (RoleItemAssociation) iter.next();
                    Item item = roleTree.getItemId();
                    String modify = null;
                    String programid = null;
                    if (roleTree.getModify() != null) {
                        if (roleTree.getDeleted() != null) {
                            // Show everything
                            modify = "2";
                        } else {
                            // Show everything except delete
                            modify = "1";
                        }
                    } else {
                        modify = "0";
                    }
                    programid = it.getItemId().toString();
                    if (item.getItemId().equals(it.getItemId()) && it.getProgramName() != null && it.getProgramName() != "{null}") {
                        itemList.add(item);
                        
                        break;
                    }
                }
            }
        return itemList;

    }

    public void getChild(List parentId, FileWriter f, Role role, HttpServletRequest request) throws Exception {
        HttpSession session = ((HttpServletRequest) request).getSession();
            for (int i = 0; i < parentId.size(); i++) {
                ItemTree itemTree = (ItemTree) parentId.get(i);

                ItemDAO itemDAO = new ItemDAO();
                Item item1 = itemDAO.findById(itemTree.getItemId());
                List itemTreeList = itemTreeDAO.getChild(item1);
                Item parent = itemDAO.findById(itemTree.getItemId());
                List itemList = new ArrayList<Item>();
                for (int j = 0; j < itemTreeList.size(); j++) {
                    ItemTree itTree = (ItemTree) itemTreeList.get(j);
                    Item it = itemDAO.findById(itTree.getItemId());
                    itemList.add(it);
                }

                List tempItemTreeList = new ArrayList();
                boolean b = true;
                for (int k = 0; k < itemTreeList.size(); k++) {
                    ItemTree itemTre = (ItemTree) itemTreeList.get(k);
                    Item child = itemDAO.findById(itemTre.getItemId());
                    if (child.getProgramName() == null || child.getProgramName().equals("{null}")) {
                        tempItemTreeList.add(itemTre);
                        String str1 = "aux" + child.getItemId() + " = insFld(aux" + parent.getItemId() + ", gFld('" + child.getItemDesc() + "', 'javascript:parent.op()'))";
                        treeList.add(str1);
                    } else {
                        Set roleTreeStructure = role.getRoleItem();
                        Iterator iter = roleTreeStructure.iterator();
                        while (iter.hasNext()) {
                            RoleItemAssociation roleTree = (RoleItemAssociation) iter.next();
                            Item item = roleTree.getItemId();
                            if (item.getItemId().equals(child.getItemId())) {
                                String str2 = "generateCheckBox(aux" + parent.getItemId() + ", '" + child.getItemDesc() + "', '" + request.getContextPath() + "/" + child.getProgramName() + "?','" + child.getItemId() + "')";
                                treeList.add(str2);
                                break;
                            }
                        }
                    }
                }
                f.write("\n\n");
                getChild(tempItemTreeList, f, role, request);
            }
    }
}
