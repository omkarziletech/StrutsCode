/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gp.cong.struts;

import com.logiware.common.dao.JobDAO;

/**
 *
 * @author Balaji.E
 */
public class LoadJobProperty {
    public static boolean getJobStatus(String property) throws Exception{
	return new JobDAO().getJobStatus(property);
    }
}
