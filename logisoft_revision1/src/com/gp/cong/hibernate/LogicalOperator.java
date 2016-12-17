/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.hibernate;

/**
 *
 * @author Administrator
 */
public enum LogicalOperator {

    OR("OR"), AND("AND");
    private String operator;

    LogicalOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return operator;
    }
};
