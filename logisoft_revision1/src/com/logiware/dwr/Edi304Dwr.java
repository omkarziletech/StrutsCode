package com.logiware.dwr;

import com.logiware.edi.xml.InttraXmlCreator;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Lakshmi Narayanan
 */
public class Edi304Dwr {

    public String createInttraXml(String fileNumber, String action, HttpServletRequest request) throws Exception {
	return new InttraXmlCreator().create(fileNumber, action, request);
    }
}
