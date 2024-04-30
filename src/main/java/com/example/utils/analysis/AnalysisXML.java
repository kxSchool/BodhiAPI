package com.example.utils.analysis;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

/**
* @author  作者 : 小布
* @version 创建时间 : 2019年5月29日 下午4:56:18
* @explain 类说明 :
*/
public class AnalysisXML {

	public static void readXMLToData() {
		try {
			 File f = new File("D:\\Mars\\Commonly\\Wechar_info\\WeChat Files\\x1355605391\\FileStorage\\File\\2019-05\\JB01.xml");
		     DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		     DocumentBuilder builder = factory.newDocumentBuilder();
		     Document doc = builder.parse(f);
		     NodeList nl = doc.getElementsByTagName("RowItem");
		     for (int i = 0; i < nl.getLength(); i++) {
		      System.out.println(doc.getElementsByTagName("patientid").item(i).getFirstChild().getNodeValue());
		     }
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
