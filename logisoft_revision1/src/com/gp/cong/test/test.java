package com.gp.cong.test;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.gp.cong.logisoft.domain.Item;
import com.gp.cong.logisoft.domain.ItemTree;
import com.gp.cong.logisoft.hibernate.dao.BaseHibernateDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemDAO;
import com.gp.cong.logisoft.hibernate.dao.ItemTreeDAO;
import org.apache.log4j.Logger;

public class test extends BaseHibernateDAO{

    private static final Logger log = Logger.getLogger(test.class);
    
    	ItemTreeDAO itemTreeDAO=new ItemTreeDAO();
	List treeList=new ArrayList();
	public List getTreeStucture(HttpServletRequest request,String role) throws Exception
	{

		FileWriter f = null;
	    String path=request.getRealPath("/jsps/admin/demoCheckboxNodes1.js");
		f=new FileWriter(path);
		f.write("USETEXTLINKS = 1\n"+"STARTALLOPEN = 0\n"+"HIGHLIGHT = 0\n"+
				"PRESERVESTATE = 1\n"+
				"USEICONS = 0\n"+

				"BUILDALL = 1\n"+
				 "function actioncheck(obj)\n"+
				  "{\n"+

				     	"var checkstr1 = \"document.getElementById('M\"+obj.id+\"')\";\n"+
				  	 	"var checkobj1 = eval(checkstr1);\n"+
				  	 	"checkobj1.checked=true;\n"+
				  	 	"checkobj1.disabled = false;\n"+

				     	"var checkstr = \"document.getElementById('D\"+obj.id+\"')\";\n"+
				  	 	"var checkobj = eval(checkstr);\n"+
				  	 	"checkobj.checked=true;\n"+
				  	 	"checkobj.disabled = false;\n"+
				  	 	"if(obj.checked==false)\n"+
				        "{\n"+
				     	  "checkobj.checked=false;\n"+
				  		  "checkobj.disabled = true;\n"+
				  		  "checkobj1.checked=false;\n"+
				  		  "checkobj1.disabled = true;\n"+
				         "}\n"+
				  	"}\n"+
				"function modifycheck(obj,modifyid)\n"+
				 " {\n"+
				 "var checkstr = \"document.getElementById('D\"+modifyid+\"')\";\n"+
					"var checkobj = eval(checkstr);\n"+
					"checkobj.checked=true;\n"+
					"checkobj.disabled = false;\n"+
				     "if(obj.checked==false)\n"+
				     "{\n"+

				  						"checkobj.checked=false;\n"+
				  						"checkobj.disabled = true;\n"+
				     "}\n"+
				  "}\n"+

				"function CA(f)\n"+"{var obj = document.getElementById('newRole')\n" +
						"for (i=0;i<obj.elements.length;i++)\n"+"{\n"+
						"if ((obj.elements[i].type == 'checkbox') && (obj.elements[i].name == f.name))\n"+
						"{if (f.checked == true) {"+"obj.elements[i].checked = true"+"}else {\n"+"obj.elements[i].checked = false\n"+
		"}}}}"+"function overlay() {"+"el = document.getElementById('overlay');\n"+
		"el.style.visibility = (el.style.visibility == 'visible') ? 'hidden' : 'visible';\n"+
		"}\n"+

		"function generateCheckBox(parentfolderObject, itemLabel, checkBoxDOMId,parentid) {\n"+
			  "var newObj;\n"+

			 " // For an explanation of insDoc and gLnk, read the online instructions.\n"+
			  "// They are the basis upon which TreeView is based.\n"+
			  "newObj = insDoc(parentfolderObject, gLnk(\"R\", \"\", \"javascript:parent.op()\"))\n"+

			  "// The trick to show checkboxes in a tree that was made to display links is to\n"+
			  "// use the prependHTML. There are general instructions about this member\n"+
			  "// in the online documentation.\n"+
			  "newObj.prependHTML = \"<td width=600px valign=middle><input type=checkbox name=\"+parentid+\"  id=\"+checkBoxDOMId+\" checked onclick='actioncheck(this)'></td>\" +\n"+
			  "\"<td >\" + itemLabel + \"&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\" +\n"+
			  "\"<td><input type=checkbox name=\"+parentid+\" id=M\" + checkBoxDOMId + \" checked onclick='modifycheck(this,\"+checkBoxDOMId+\")'></td><td><font color=black>Modify&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\" +\n"+
			  "\"<td><input type=checkbox name=\"+parentid+\" id=D\" + checkBoxDOMId + \" checked></td><td><font color=black>Delete&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>\" +\n"+
			  "\"<td><img src='Lock4.gif' name='addRest' value='Add Restriction' img src='addrestriction.gif' border=0 onClick='overlay()' ></td>\"\n"+

			  "}\n"+

			  "function generateTopCheckBox(parentfolderObject, itemLabel, groupname, checkid) {\n"+
					"var newObj;\n"+

					"// Read the online documentation for an explanation of insDoc and gLnk,\n"+
				    "// they are the base of the simplest Treeview trees\n"+
					"newObj = insDoc(parentfolderObject, gLnk(\"R\", \"\", \"\"))\n"+

				   " // The trick to show checkboxes in a tree that was made to display links is to\n"+
					"// use the prependHTML. There are general instructions about this member\n"+
				    "// in the online documentation\n"+
					"var lyrstr = 1;\n"+
					"newObj.prependHTML = \"<td valign=middle><input type=checkbox name='\" + groupname + \"' id='\"+ groupname + \"' onclick='CA(this)'></td>\" +\n"+
					"\"<td>\" + itemLabel + \"</td>\"}\n"+

					"foldersTree = gFld(\"Econocaribe Menu Creator\", \"\")\n"+
					"foldersTree.treeID = \"checkboxTree\"\n"+
					"aux1 = insFld(foldersTree, gFld(\"Logisoft\", \"javascript:parent.op()\"))\n"
);

		ItemTree itemTree =new ItemTree();
		itemTree.setItemId(1);
		Item item=new Item();
		item.setItemId(-1);
		itemTree.setParentId(item);
		List itemTreeList=new ArrayList();
		itemTreeList.add(itemTree);
		getChild(itemTreeList,f,role);
	    f.close();
		return treeList;
	}
    public void getChild(List parentId,FileWriter f,String role)
    {

    	try
    	{

    	for(int i=0;i<parentId.size();i++)
    	{
    		 ItemTree itemTree=(ItemTree)parentId.get(i);

        	 ItemDAO itemDAO=new ItemDAO();
        	 Item item1=itemDAO.findById(itemTree.getItemId());
        	 List itemTreeList=itemTreeDAO.getChild(item1);


        	 Item parent=itemDAO.findById(itemTree.getItemId());
    	 List itemList=new ArrayList<Item>();
		 int id = 0;
    	 for(int j=0;j<itemTreeList.size();j++)
    	 {
    		 ItemTree itTree=(ItemTree)itemTreeList.get(j);
    		 Item it=itemDAO.findById(itTree.getItemId());
    		 itemList.add(it);
    	 }
    	 if(!role.equals("Admin") ){
    		 if(parent.getItemDesc().equals("Menu Management")){

    		 }else{
        	 String str = "aux" + parent.getItemId() + " = insFld(foldersTree, gFld('" + parent.getItemDesc() + "', 'javascript:parent.op()'))";
        	// f.write(str);
        	 //f.write("\n");
    	 //}
    	 List tempItemTreeList=new ArrayList();
    	 boolean b=true;
    	 for(int k=0; k<itemTreeList.size(); k++){
    		 ItemTree itemTre=(ItemTree)itemTreeList.get(k);
    		 Item child=itemDAO.findById(itemTre.getItemId());
    		if(child.getProgramName()==null || child.getProgramName().equals("{null}"))
    		 {

    			 //f.write("---child--->" + child.getItemDesc() + "\n");
    			 tempItemTreeList.add(itemTre);
    			 String str1 = "aux" + child.getItemId() + " = insFld(aux"+parent.getItemId()+", gFld('" + child.getItemDesc() + "', 'javascript:parent.op()'))";
    			 treeList.add(str1);
    			 f.write(str1);
    			 f.write("\n");
    		 }
    		 else
    		{

   			  if(b)
   			  {
   				  String str1 = "generateTopCheckBox(aux"+parent.getItemId()+", 'checkAll', '"+parent.getItemId()+"', 1)";
   				  treeList.add(str1);
   				  f.write(str1);
   				  f.write("\n");
   				  b=false;
   			  }
   			  String str2 = "generateCheckBox(aux"+parent.getItemId()+", '"+child.getItemDesc()+"', '"+child.getItemId()+"','"+parent.getItemId()+"')";
   			 treeList.add(str2);
				  f.write(str2);
				  f.write("\n");
           	 //f.write("-------Action--->" + child.getItemDesc() + "\n");
    		}

    	 }

    	 f.write("\n\n");
    	 getChild(tempItemTreeList,f,role);
    	 }
    	 }else{

        	 String str = "aux" + parent.getItemId() + " = insFld(foldersTree, gFld('" + parent.getItemDesc() + "', 'javascript:parent.op()'))";
        	// f.write(str);
        	 //f.write("\n");
    	 //}
    	 List tempItemTreeList=new ArrayList();
    	 boolean b=true;
    	 for(int k=0; k<itemTreeList.size(); k++){
    		 ItemTree itemTre=(ItemTree)itemTreeList.get(k);
    		 Item child=itemDAO.findById(itemTre.getItemId());
    		if(child.getProgramName()==null || child.getProgramName().equals("{null}"))
    		 {

    			 //f.write("---child--->" + child.getItemDesc() + "\n");
    			 tempItemTreeList.add(itemTre);
    			 String str1 = "aux" + child.getItemId() + " = insFld(aux"+parent.getItemId()+", gFld('" + child.getItemDesc() + "', 'javascript:parent.op()'))";
    			 treeList.add(str1);
    			 f.write(str1);
    			 f.write("\n");
    		 }
    		 else
    		{

   			  if(b)
   			  {
   				  String str1 = "generateTopCheckBox(aux"+parent.getItemId()+", 'checkAll', '"+parent.getItemId()+"', 1)";
   				  treeList.add(str1);
   				  f.write(str1);
   				  f.write("\n");
   				  b=false;
   			  }
   			  String str2 = "generateCheckBox(aux"+parent.getItemId()+", '"+child.getItemDesc()+"', '"+child.getItemId()+"','"+parent.getItemId()+"')";
   			 treeList.add(str2);
				  f.write(str2);
				  f.write("\n");
           	 //f.write("-------Action--->" + child.getItemDesc() + "\n");
    		}

    	 }

    	 f.write("\n\n");
    	 getChild(tempItemTreeList,f,role);

    	 }
        }
    	}
    	catch(Exception e)
    	{
    		log.info("Error when writing file in test class....." ,e);
    	}

    }

   /* public int whoIsTheParent(int child){


    	return parent;
    }*/


	public static void main(String[] args)
	{

			String s = "<message>It is eroro meesag</message>";
                        log.info(s.substring(s.indexOf("<message>")+9, s.indexOf("</message>")));
			


	}

}
