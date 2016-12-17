package com.logiware.edi.tracking;

import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.directwebremoting.WebContextFactory;

import com.gp.cong.common.CommonConstants;
import com.gp.cong.common.DateUtils;
import java.util.Date;

public class EdiSystemDwr {

    public String showMoreInfoForEdi(EdiSystemBean ediBean) throws Exception {
        HttpServletRequest request = WebContextFactory.get().getHttpServletRequest();
        if (null != ediBean) {
            request.setAttribute("ediBean", ediBean);
            request.setAttribute("filePath", getFilePath(ediBean));
            return WebContextFactory.get().forwardToString("/jsps/EdiTrackingSystem/EdiTrackingTemplate.jsp");
        } else {
            return null;
        }
    }

    private String getFilePath(EdiSystemBean ediBean) throws Exception {
        String filePath = null;
        Properties prop = new Properties();
        prop.load(getClass().getResourceAsStream(CommonConstants.EDITRACKINGPROPERTIES));
        String osName = System.getProperty("os.name").toLowerCase();
        String messageType = ediBean.getMessageType();
        String ediCompany = ediBean.getEdiCompany();
        String fileName = ediBean.getFileName();
        String status = ediBean.getStatus();
        String processedDate = ediBean.getProcessedDate();
        String dateFolder = DateUtils.formatDate(new Date(), "yyyy/MM/dd") + "/";
        if (null != messageType && messageType.trim().equals("997")) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttra997Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("inttra997Archive") + dateFolder + fileName;
                    }
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttra997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("inttra997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            } else if (null != ediCompany && ediCompany.trim().equals("GTNEXUS")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexus997Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("gtnexus997Archive") + dateFolder + fileName;
                    }
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexus997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("gtnexus997LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            }
        } else if (null != messageType && messageType.trim().equals("304")) {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttraXmlOut") + fileName;
                    } else {
                        filePath = prop.getProperty("inttraXmlOut") + fileName;
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
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexusLogFileOut") + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("gtnexusLogFileOut") + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            }
        } else {
            if (null != ediCompany && ediCompany.trim().equals("INTTRA")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttra315Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("inttra315Archive") + dateFolder + fileName;
                    }
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxInttra315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("inttra315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            } else if (null != ediCompany && ediCompany.trim().equals("GTNEXUS")) {
                if (null != status && status.trim().equals("success")) {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexus315Archive") + dateFolder + fileName;
                    } else {
                        filePath = prop.getProperty("gtnexus315Archive") + dateFolder + fileName;
                    }
                } else {
                    if (osName.contains("linux")) {
                        filePath = prop.getProperty("linuxGtnexus315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    } else {
                        filePath = prop.getProperty("gtnexus315LogFile") + dateFolder + "error_logfile_" + fileName + "_" + processedDate + ".txt";
                    }
                }
            }
        }
        return filePath;
    }
}
