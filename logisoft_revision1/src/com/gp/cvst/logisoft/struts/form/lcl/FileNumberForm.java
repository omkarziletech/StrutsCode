/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gp.cvst.logisoft.struts.form.lcl;

import com.gp.cong.logisoft.domain.lcl.LclFileNumber;

/**
 *
 * @author Owner
 */
public class FileNumberForm extends LogiwareActionForm {

  private LclFileNumber fileNumber;

  public FileNumberForm() {
    this.fileNumber = new LclFileNumber();
  }

  public LclFileNumber getFileNumber() {
    if (fileNumber == null) {
      fileNumber = new LclFileNumber();
    }
    return fileNumber;
  }

  public void setFileNumber(LclFileNumber fileNumber) {
    this.fileNumber = fileNumber;
  }
}
