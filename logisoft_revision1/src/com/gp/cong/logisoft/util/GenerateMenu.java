package com.gp.cong.logisoft.util;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ItemTree;
import com.gp.cong.logisoft.domain.Role;
import com.gp.cong.logisoft.domain.RoleItemAssociation;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemTreeDAO;

public class GenerateMenu extends BaseHibernateDAO {

    ItemTreeDAO itemTreeDAO = new ItemTreeDAO();
    List treeList = new ArrayList();

    public List getMenuStucture(HttpServletRequest request, Role role) throws Exception {
        FileWriter f = null;
        String path = request.getSession().getServletContext().getRealPath("Menu2.html");
        f = new FileWriter(path);
        ItemTree itemTree = new ItemTree();
        itemTree.setItemId(1);
        Item item = new Item();
        item.setItemId(0);
        itemTree.setParentId(item);
        List itemTreeList = new ArrayList();
        itemTreeList.add(itemTree);

        getChild(itemTreeList, f, role, request);
        f.close();
        return treeList;
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
            int id = 0;
            for (int j = 0; j < itemTreeList.size(); j++) {
                ItemTree itTree = (ItemTree) itemTreeList.get(j);
                Item it = itemDAO.findById(itTree.getItemId());
                itemList.add(it);
            }

            /*if(itemTreeList.size() <= 0) {

            f.write("-------Action--->" + parent.getItemDesc() + "\n");
            }else {*/

            //f.write("aux0000 = insFld(foldersTree, gFld(\"" + parent.getItemDesc() + "\n");
            String str = "aux" + parent.getItemId() + " = insFld(foldersTree, gFld('" + parent.getItemDesc() + "', 'javascript:parent.op()','" + request.getContextPath() + "+))";
            // f.write(str);
            //f.write("\n");
            //}
            List tempItemTreeList = new ArrayList();
            boolean b = true;
            boolean menu = false;
            String tempStr = "";
            for (int k = 0; k < itemTreeList.size(); k++) {
                ItemTree itemTre = (ItemTree) itemTreeList.get(k);
                Item child = itemDAO.findById(itemTre.getItemId());
                if (child.getProgramName() == null || child.getProgramName().equals("{null}")) {

                    //f.write("---child--->" + child.getItemDesc() + "\n");
                    tempItemTreeList.add(itemTre);
                    tempStr = new String("aux" + child.getItemId() + " = insFld(aux" + parent.getItemId() + ", gFld('" + child.getItemDesc() + "', \"javascript:parent.op()\",'" + child.getItemId() + "','" + request.getContextPath() + "'))");

                    // f.write(str1);
                    //f.write("\n");
                    treeList.add(tempStr);
                } else {

                    Set roleTreeStructure = role.getRoleItem();
                    Iterator iter = null;
                    if (roleTreeStructure != null) {
                        iter = roleTreeStructure.iterator();
                        menu = true;
                        while (iter.hasNext()) {
                            RoleItemAssociation roleTree = (RoleItemAssociation) iter.next();
                            Item item = roleTree.getItemId();

                            String modify = null;
                            if (roleTree.getModify() != null) {
                                if (roleTree.getDeleted() != null) {
                                    // Show only delete
                                    modify = "2";
                                } else {
                                    // Show only edit
                                    modify = "1";
                                }

                            } else {
                                // Show only view
                                modify = "0";
                            }






                            if (item.getItemId().equals(child.getItemId())) {



                                String inputStr = child.getItemDesc();
                                String patternStr = " ";
                                String replacementStr = "_";

                                // Compile regular expression
                                Pattern pattern = Pattern.compile(patternStr);

                                // Replace all occurrences of pattern in input
                                Matcher matcher = pattern.matcher(inputStr);
                                String getItemDesc = matcher.replaceAll(replacementStr);




                                String str2 = "generateCheckBox(aux" + parent.getItemId() + ", '" + getItemDesc + "', '" + request.getContextPath() + "/" + child.getProgramName() + "?modify=" + modify + "','" + child.getItemId() + "','" + parent.getItemId() + "')";
                                // f.write(str2);
                                // f.write("\n");
                                treeList.add(str2);
                                // f.write(str2);
                                // f.write("\n");

                                b = false;
                                break;
                            }
                        }


                    }

                    //f.write("-------Action--->" + child.getItemDesc() + "\n");
                }



            }
            if (b && menu) {

                treeList.remove(tempStr);
            }
            f.write("\n\n");
            getChild(tempItemTreeList, f, role, request);
        }
    }

    /* public int whoIsTheParent(int child){


    return parent;
    }*/
    public Map getMenuStructure() throws Exception {
        Map<Item, List> menuMap = new LinkedHashMap<Item, List>();
        ItemDAO itemDAO = new ItemDAO();
        List<Item> rootNodes = itemDAO.getChild(1);
        for (Iterator iter = rootNodes.iterator(); iter.hasNext();) {
            Item item = (Item) iter.next();
            List<Item> childItems = itemDAO.getChild(item.getItemId());
            menuMap.put(item, childItems);
        }
        return menuMap;
    }

    public List<Item> getChildren(Item item)throws Exception {
        ItemDAO itemDAO = new ItemDAO();
        List<Item> childItems = itemDAO.getChild(item.getItemId());
        return childItems;
    }

    public Item getFirstItemFromParent(Item item)throws Exception {
        ItemDAO itemDAO = new ItemDAO();
        Item childItem = null;
        List<Item> subChildList = itemDAO.getChild(item.getItemId());
        if (subChildList != null && !subChildList.isEmpty()) {
            childItem = subChildList.get(0);
        }
        return childItem;
    }
}
