package com.gp.cong.logisoft.util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ItemTree;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RoleItemAssociation;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemTreeDAO;

public class Edit extends BaseHibernateDAO {

    ItemTreeDAO itemTreeDAO = new ItemTreeDAO();
    List treeList = new ArrayList();

    public List getMenuStucture(HttpServletRequest request, Role role) throws Exception{
        FileWriter f = null;
            String path = request.getRealPath("Menu2.html");
            f = new FileWriter(path);
            ItemTree itemTree = new ItemTree();
            itemTree.setItemId(1);
            Item item = new Item();
            item.setItemId(-1);
            itemTree.setParentId(item);
            List itemTreeList = new ArrayList();
            itemTreeList.add(itemTree);

            getChild(itemTreeList, f, role, request);
            f.close();
        return treeList;
    }

    public void getChild(List parentId, FileWriter f, Role role, HttpServletRequest request)throws Exception{
            for (int i = 0; i < parentId.size(); i++) {
                ItemTree itemTree = (ItemTree) parentId.get(i);
                ItemDAO itemDAO = new ItemDAO();
                Item item1 = itemDAO.findById(itemTree.getItemId());
                List itemTreeList = itemTreeDAO.getChild(item1);
                Item parent = itemDAO.findById(itemTree.getItemId());
                List itemList = new ArrayList<Item>();
                int id = 0;
                for (int j = 0; j < itemTreeList.size(); j++) {
                    ItemTree itTree = (ItemTree) itemTreeList.get(j);
                    if(null!=itTree.getItemId()){
                        Item it = itemDAO.findById(itTree.getItemId());
                        itemList.add(it);
                    }
                }
                boolean flag = false;
                if (!role.getRoleDesc().equals("Admin")) {
                    if (parent.getItemDesc().equals("Menu Management")) {
                    } else {
                        String str = "aux" + parent.getItemId() + " = insFld(foldersTree, gFld('" + parent.getItemDesc() + "', 'javascript:parent.op()'))";
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
                                boolean found = false;
                                if (b) {
                                    String str1 = "generateTopCheckBox(aux" + parent.getItemId() + ", 'checkAll', '" + parent.getItemId() + "', 1)";

                                    treeList.add(str1);
                                    b = false;
                                }
                                Set roleTreeStructure = role.getRoleItem();
                                Iterator iter = roleTreeStructure.iterator();
                                while (iter.hasNext()) {
                                    RoleItemAssociation roleTree = (RoleItemAssociation) iter.next();
                                    Item item = roleTree.getItemId();
                                    if (item.getItemId().equals(child.getItemId())) {
                                        String modify = "";
                                        String delete = "";
                                        if (roleTree.getModify() != null && roleTree.getModify().equals("modify")) {
                                            modify = "checked";
                                            if (roleTree.getDeleted() != null && roleTree.getDeleted().equals("delete")) {
                                                delete = "checked";
                                            }
                                        }
                                        String str2 = "generateCheckBox(aux" + parent.getItemId() + ", '" + child.getItemDesc() + "', '" + child.getItemId() + "','checked','" + modify + "','" + delete + "','" + parent.getItemId() + "')";
                                        treeList.add(str2);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    String str2 = "generateCheckBox(aux" + parent.getItemId() + ", '" + child.getItemDesc() + "', '" + child.getItemId() + "','','','','" + parent.getItemId() + "')";
                                    treeList.add(str2);
                                }
                            }

                        }
                        f.write("\n\n");
                        getChild(tempItemTreeList, f, role, request);
                    }
                } else {
                    String str = "aux" + parent.getItemId() + " = insFld(foldersTree, gFld('" + parent.getItemDesc() + "', 'javascript:parent.op()'))";
                    List tempItemTreeList = new ArrayList();
                    boolean b = true;
                    for (int k = 0; k < itemTreeList.size(); k++) {
                        ItemTree itemTre = (ItemTree) itemTreeList.get(k);
                        if(null!=itemTre.getItemId()){
                            Item child = itemDAO.findById(itemTre.getItemId());
                            if (child.getProgramName() == null || child.getProgramName().equals("{null}")) {
                                tempItemTreeList.add(itemTre);
                                String str1 = "aux" + child.getItemId() + " = insFld(aux" + parent.getItemId() + ", gFld('" + child.getItemDesc() + "', 'javascript:parent.op()'))";
                                treeList.add(str1);
                            } else {
                                boolean found = false;
                                if (b) {
                                    String str1 = "generateTopCheckBox(aux" + parent.getItemId() + ", 'checkAll', '" + parent.getItemId() + "', 1)";

                                    treeList.add(str1);
                                    b = false;
                                }
                                Set roleTreeStructure = role.getRoleItem();
                                Iterator iter = roleTreeStructure.iterator();
                                while (iter.hasNext()) {
                                    RoleItemAssociation roleTree = (RoleItemAssociation) iter.next();
                                    Item item = roleTree.getItemId();
                                    if (item.getItemId().equals(child.getItemId())) {
                                        String modify = "";
                                        String delete = "";
                                        if (roleTree.getModify() != null && roleTree.getModify().equals("modify")) {
                                            modify = "checked";
                                            if (roleTree.getDeleted() != null && roleTree.getDeleted().equals("delete")) {
                                                delete = "checked";
                                            }
                                        }
                                        String str2 = "generateCheckBox(aux" + parent.getItemId() + ", '" + child.getItemDesc() + "', '" + child.getItemId() + "','checked','" + modify + "','" + delete + "','" + parent.getItemId() + "')";
                                        treeList.add(str2);
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    String str2 = "generateCheckBox(aux" + parent.getItemId() + ", '" + child.getItemDesc() + "', '" + child.getItemId() + "','','','','" + parent.getItemId() + "')";
                                    treeList.add(str2);
                                }
                            }
                        }

                    }
                    f.write("\n\n");
                    getChild(tempItemTreeList, f, role, request);
                }
            }
    }

    /* public int whoIsTheParent(int child){


    return parent;
    }*/
    public static void main(String[] args) {
    }
}
