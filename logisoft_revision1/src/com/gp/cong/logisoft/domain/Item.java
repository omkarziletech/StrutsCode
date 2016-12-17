package com.gp.cong.logisoft.domain;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

public class Item implements Auditable, Serializable {

    private Integer itemId;
    private String itemDesc;
    private String programName;
    private Date itemcreatedon;
    private Set itemTree;
    private String itemType;
    private String predecessor;
    private String parentId;
    private String itemcreateddate;
    private String uniqueCode;

    public String getUniqueCode() {
        return uniqueCode;
    }

    public void setUniqueCode(String uniqueCode) {
        this.uniqueCode = uniqueCode;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getPredecessor() {
        /*if(itemTree!=null)
         {
         Iterator iter=itemTree.iterator();
         while(iter.hasNext())
         {
         ItemTree itemTree=(ItemTree)iter.next();
         Item item=itemTree.getParentId();
				
         if(item!=null)
         {	
				   
         return item.getItemDesc();
         }
         }
         }*/
        return predecessor;
    }

    public void setPredecessor(String predecessor) {

        this.predecessor = predecessor;
    }

    public String getItemType() {
        if (programName == null) {
            return "Menu";
        } else {
            return "Action";
        }



    }

    public void setItemType(String itemType) {

        this.itemType = itemType;
    }

    public String getItemDesc() {

        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getProgramName() {

        return programName;
    }

    public void setProgramName(String programName) {
        this.programName = programName;
    }

    public Set getItemTree() {
        return itemTree;
    }

    public void setItemTree(Set itemTree) {
        this.itemTree = itemTree;
        if (itemTree != null) {
            Iterator iter = itemTree.iterator();
            while (iter.hasNext()) {
                ItemTree itemTree1 = (ItemTree) iter.next();

                Item item = itemTree1.getParentId();

                if (item != null) {

                    Integer parentId = item.getItemId();
                    this.setParentId(String.valueOf(parentId));
                    this.setPredecessor(item.getItemDesc());

                }
            }
        }
    }

    public Date getItemcreatedon() {
        return itemcreatedon;
    }

    public void setItemcreatedon(Date itemcreatedon) {
        this.itemcreatedon = itemcreatedon;
    }

    public String getItemcreateddate() {
        if (itemcreatedon != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy hh:mm:ss");
            return sdf.format(itemcreatedon);
        }
        return itemcreateddate;
    }

    public void setItemcreateddate(String itemcreateddate) {
        this.itemcreateddate = itemcreateddate;
    }

    public AuditInfo getAuditInfo() {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getId() {
        // TODO Auto-generated method stub
        return this.getItemId();
    }
}
