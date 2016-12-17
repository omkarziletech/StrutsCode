package com.gp.cong.logisoft.domain;

import java.io.Serializable;

public class TerminalContacts implements Serializable {

    private int id;
    private String terminalid;
    private int contactid;
    private RefTerminal terminal;
    private User user;
    private String contactemail;
    private String contactName;

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {

        this.contactName = contactName;
    }

    public String getContactemail() {
        return contactemail;
    }

    public void setContactemail(String contactemail) {
        this.contactemail = contactemail;
    }

    public int getContactid() {
        return contactid;
    }

    public void setContactid(int contactid) {
        this.contactid = contactid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTerminalid() {
        return terminalid;
    }

    public void setTerminalid(String terminalid) {
        this.terminalid = terminalid;
    }

    public RefTerminal getTerminal() {
        return terminal;
    }

    public void setTerminal(RefTerminal terminal) {
        this.terminal = terminal;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }
}
