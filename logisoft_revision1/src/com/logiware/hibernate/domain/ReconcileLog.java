/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.logiware.hibernate.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author logiware
 */
@Entity
@Table(name = "reconcile_log")
public class ReconcileLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Column(name = "gl_account")
    private String glAccount;
    @Column(name = "bank_id")
    private Integer bankId;
    @Column(name = "reconcile_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date reconcileDate;
    @Column(name = "balance")
    private Double balance = 0d;

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public Integer getBankId() {
        return bankId;
    }

    public void setBankId(Integer bankId) {
        this.bankId = bankId;
    }

    public String getGlAccount() {
        return glAccount;
    }

    public void setGlAccount(String glAccount) {
        this.glAccount = glAccount;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getReconcileDate() {
        return reconcileDate;
    }

    public void setReconcileDate(Date reconcileDate) {
        this.reconcileDate = reconcileDate;
    }
}
