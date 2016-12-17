 package com.logiware.bean; 

import java.util.List;
import java.util.Objects;

/**
 *
 * @author omi
 */ 
public class User {
    
    private int id;
    private String name;
    private String lname;
    private String gender;   
    private String post;
    private String phone;
    private List<String> extraDetailList;

    public List<String> getExtraDetailList() {
        return extraDetailList;
    }

    public void setExtraDetailList(List<String> extraDetailList) {
        this.extraDetailList = extraDetailList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getPhone() {
        return phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
 
    public User clone(){
	User u = new User();        
	u.setId(this.getId());
        u.setName(this.getName());
	u.setLname(this.getLname());
        u.setGender(this.getGender());
        
        u.setPost(this.getPost());
        u.setPhone(this.getPhone());           
	return u;
}

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.id;
        hash = 23 * hash + Objects.hashCode(this.name);
        hash = 23 * hash + Objects.hashCode(this.lname);
        hash = 23 * hash + Objects.hashCode(this.gender);
        hash = 23 * hash + Objects.hashCode(this.post);
        hash = 23 * hash + Objects.hashCode(this.phone);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final User other = (User) obj;
        if (this.id != other.id) {
            return false;
        }
       
        return true;
    }
    
    
 
 
}