package com.gp.cong.logisoft.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

public class LoginUser implements Serializable{
    private Hashtable loginTimes;
    private Hashtable loggedOn;

    public LoginUser() {
        loggedOn = new Hashtable();
        loginTimes = new Hashtable();
    }

    public boolean userAlreadyLoggedOn(String userid) {
        if (loggedOn.containsKey(userid)) {
            return true;
        } else {
            return false;
        }
    }

    public Vector getUserDetails(String userid) {

        return (Vector) loggedOn.get(userid);
    }

    public void removeUser(String userid) {
        loggedOn.remove(userid);
        loginTimes.remove(userid);
    }

    public void loginUser(String userid, Vector userdetails) {
        loggedOn.put(userid, userdetails);
        loginTimes.put(userid, new Date());
    }

    public Hashtable allElements() {

        return loggedOn;

    }

    public Hashtable getLoginTimes() {
        return loginTimes;
    }
}
