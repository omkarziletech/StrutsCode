/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cong.logisoft.bc.tradingpartner;

/**
 *
 * @author Logiware
 */
public enum SortByEnumforTP {
   

    accountName("t.accountName"), accountNumber("t.accountNumber"), accountType("t.accountType"),
    subType("t.subType"), creditStatus("cacc.credit_status"),eciAcctNo3List("eciAcctNo3List"),eciAcctNo2List("eciAcctNo2List"),eciAcctNoList("eciAcctNoList"),
    creditLimit("cacc.credit_limit"), colleCtor("colleCtor"), address("ca.address1"), city("ca.city1"), state("ca.state");
  
    private String field;

    public String getField() {
        return field;
    }

    private SortByEnumforTP(String field) {
        this.field = field;
    }

}
