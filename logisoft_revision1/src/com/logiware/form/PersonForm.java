package com.logiware.form;

import com.logiware.bean.User;
import java.util.ArrayList;
import java.util.List;
import org.apache.struts.action.ActionForm;
/**
 *
 * @author omi
 */
@SuppressWarnings("serial")
public class PersonForm extends ActionForm {
    
     User user=new User();  
     private List<User> userList;
     private List<User> userSearchList;
     private String byName;
     private String action;

     public PersonForm(){
         this.userList = new ArrayList<>();
         this.userSearchList=new ArrayList<>(); 
     }
    

    public String getByName() {      
        return byName;          
    }

    public void setByName(String byName) {
        this.byName = byName;
    }
    
    
    
    public int getId() {
        return user.getId();
    }

    public void setId(int id) {
        this.user.setId(id);
    }
    
    public String getName() {
        return user.getName();
    }

    public void setName(String name) {
        this.user.setName(name);
    }

    public String getLname() {
        return user.getLname();
    }
    public void setLname(String lname) {
        this.user.setLname(lname);
    }
    public String getGender() {
        return user.getGender();
    }

    public void setGender(String gender) {
        this.user.setGender(gender);
    }
   
    public String getPost() {
        return user.getPost();
    }

    public void setPost(String post) {
        this.user.setPost(post);
    }

    public String getPhone() {
        return user.getPhone();
    }
    public void setPhone(String phone) {
        this.user.setPhone(phone);
    }   
    public List<String> getExtraDetailList() {
        return user.getExtraDetailList();
    }

    public void setExtraDetailList(List<String> extraDetailList) {
        this.user.setExtraDetailList(extraDetailList);
    }
    
    public User getUser() {
        return this.user;
    }
    public void setUser(User user) {
        user = this.user;
    }   
    public List<User> getUserList() {
        return userList;
    }
    public void setUserList(List<User> userList) {
        this.userList = userList;
    }
    public void add(User user) {
       
        this.userList.add(user);
    }
    public List<User> getUserSearchList() {         
    return userSearchList;
    } 

    public void setUserSearchList(List<User> userSearchList) {      
        this.userSearchList = userSearchList;
    }
     public void searchAdd(User user) { 
        this.userSearchList.add(user);
    }
    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
    
    
}
