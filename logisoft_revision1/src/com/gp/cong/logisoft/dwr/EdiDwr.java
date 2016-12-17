package com.gp.cong.logisoft.dwr;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;
import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.CommonUtils;
import com.gp.cong.common.DateUtils;
import com.gp.cong.logisoft.beans.EdiBean;
import java.io.File;

public class EdiDwr {

    public void showMoreInfoForEdi(EdiBean ediBean, HttpServletRequest request) throws Exception {
        if (null != ediBean) {
            request.setAttribute("ediBean", ediBean);
            request.setAttribute("filePath", getFilePath(ediBean));
        }
    }

    private String getFilePath(EdiBean ediBean) throws Exception {
        String filePath = null;
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream(CommonConstants.EDIPROPERTIES));
        String osName = System.getProperty("os.name").toLowerCase();
        String messageType = ediBean.getMessageType();
        String docTyp = ediBean.getDocTyp();
        String ediCompany = ediBean.getEdiCompany();
        String fileName = ediBean.getFileName();
        String status = ediBean.getStatus();
        String processedDate = ediBean.getProcessedDate();
        String dateFolder = DateUtils.formatDate(DateUtils.parseDate(processedDate, "yyyyMMddHHmmss"), "yyyy/MM/dd")+ "/";
          if (null != messageType && messageType.trim().equals("997") && docTyp.equals("booking")) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
//                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxBookingInttra997Archive")  + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("inttraBooking997Archive")  + dateFolder + fileName;
                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxBookingInttra997LogFile")  + dateFolder +  "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    } else {
//                        filePath = prop.getProperty("inttraBooking997LogFile")  + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    }
//                }
            } 
        }else if (null != messageType && messageType.trim().equals("997") ) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
//                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttra997Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("inttra997Archive") + dateFolder + fileName;
                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxInttra997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    } else {
//                        filePath = prop.getProperty("inttra997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    }
//                }
            } else if (null != ediCompany && ediCompany.trim().equals("GTNEXUS")) {
//                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexus997Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("gtnexus997Archive") + dateFolder + fileName;
                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxGtnexus997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    } else {
//                        filePath = prop.getProperty("gtnexus997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    }
//                }
            }
        } else if (null != messageType && messageType.trim().equals("304")) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttraXmlOut") + fileName;
                    } else {
                        filePath = prop.getProperty("inttraXmlOut") + fileName;
                    }
                    File sourceFile = new File(filePath);
                    if (!sourceFile.exists()) {
                        if (osName.contains("linux")) {
                            filePath = prop.getProperty("linuxInttra304Archive") + fileName;
                        } else {
                            filePath = prop.getProperty("inttra304Archive") + fileName;
                        }

                    }
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttraLogFileOut") + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("inttraLogFileOut") + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            } else if (null != ediCompany && ediCompany.trim().equals("GTNEXUS")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexusXmlOut") + fileName;
                    } else {
                        filePath = prop.getProperty("gtnexusXmlOut") + fileName;
                    }
                    File sourceFile = new File(filePath);
                    if (!sourceFile.exists()) {
                        if (osName.contains("linux")) {
                            filePath = prop.getProperty("linuxGtnexus304Archive") + fileName;
                        } else {
                            filePath = prop.getProperty("gtnexus304Archive") + fileName;
                        }

                    }
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexusLogFileOut") + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("gtnexusLogFileOut") + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            }
        } else if (null != messageType && (messageType.trim().equals("300"))) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
//                if (CommonUtils.in(status,"success","change","cancel")) {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxInttra300XmlOut")  + fileName;
//                    } else {
//                        filePath = prop.getProperty("inttra300XmlOut")  + fileName;
//                    }
//                    File sourceFile = new File(filePath);
//                    if (!sourceFile.exists()) {
                        if (osName.contains("linux")) {
                            filePath = prop.getProperty("linuxInttra300Archive")  + dateFolder + fileName;
                        } else {
                            filePath = prop.getProperty("inttra300Archive") + dateFolder + fileName;
                        }

//                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxInttra300LogFileOut")  + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    } else {
//                        filePath = prop.getProperty("inttra300LogFileOut")  + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    }
//                }
            }
        }else if (null != messageType && messageType.trim().equals("301")) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
//                if (CommonUtils.in(status.trim(),"success","pending","conditionally accepted","cancel","replaced","declined")) {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxInttra301Archive") + dateFolder + fileName;
//                    } else {
//                        filePath = prop.getProperty("inttra301Archive") + dateFolder + fileName;
//                    }
//                    File sourceFile = new File(filePath);
//                    if (!sourceFile.exists()) {
                        if (osName.contains("linux")) {
                            filePath = prop.getProperty("linuxInttra301Archive") + dateFolder + fileName;
                        } else {
                            filePath = prop.getProperty("inttra301Archive") + dateFolder + fileName;
                        }

//                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxInttra301LogFile") + dateFolder + fileName + ".txt";
//                    } else {
//                        filePath = prop.getProperty("inttra301LogFile") + dateFolder + fileName + ".txt";
//                    }
//                }
            }
        } else {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
//                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttra315Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("inttra315Archive") + dateFolder + fileName;
                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxInttra315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    } else {
//                        filePath = prop.getProperty("inttra315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    }
//                }
            } else if (null != ediCompany && ediCompany.trim().equals("GTNEXUS")) {
//                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexus315Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("gtnexus315Archive") + dateFolder + fileName;
                    }
//                } else {
//                    if (osName.contains("linux")) {
//                        filePath = prop.getProperty("linuxGtnexus315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    } else {
//                        filePath = prop.getProperty("gtnexus315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
//                    }
//                }
            }
        }
        return filePath;
    }
}
