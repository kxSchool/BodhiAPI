package com.example.utils.analysis;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 作者 : 小布
 * @version 创建时间 : 2019年5月29日 下午6:05:45
 * @explain 类说明 :
 */
public class XMLToMap {

    public static String stripNonValidXMLChars(String str) {
        if (str == null || "".equals(str)) {
            return str;
        }
        return str.replaceAll("[\\x00-\\x08\\x0b-\\x0c\\x0e-\\x1f]", "");
    }

    public static Map<String, Object> dataXMLTOMap(String valueXML) throws DocumentException {
        // 解析返回的xml字符串，生成document对象
        Document document = DocumentHelper.parseText(valueXML);
        // 根节点
        Element root = document.getRootElement();
        // 子节点
        List<Element> childElements = root.elements();

        Map<String, Object> mapEle = new ConcurrentHashMap<String, Object>();
        return mapEle = getAllElements(childElements, mapEle);
    }

    public static Map<String, Object> getAllElements(List<Element> childElements, Map<String, Object> mapEle) {
        for (Element ele : childElements) {
            mapEle.put(ele.getName(), ele.getText());
            if (ele.elements().size() > 0) {
                mapEle = getAllElements(ele.elements(), mapEle);
            }
        }
        return mapEle;
    }

    // xml转map
    public static Map<String, Object> mainMethod(ByteArrayInputStream byteArrayInputStream) {
        Map<String, Object> map_finall = new ConcurrentHashMap<String, Object>(10000000);
        try {
            SAXReader saxreader = new SAXReader();
            Document doc = saxreader.read(byteArrayInputStream);
            Element rootElement = doc.getRootElement();
            // 调用递归方法
            map_finall.put(rootElement.getName(), DiGui(rootElement));
        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return map_finall;
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
   String RetXml = "<WinResponse><RowItem><sqdxh>2329887</sqdxh><hzxm>陶信水</hzxm><jcmd>明确诊断</jcmd><lczd>乙肝后肝硬化</lczd><jcxm>电脑多导联心电图</jcxm><bszl>乙肝后肝硬化</bszl></RowItem><RowItem><sqdxh>2329890</sqdxh><hzxm>陶信水</hzxm><jcmd>明确诊断</jcmd><lczd>乙肝后肝硬化</lczd><jcxm>胸部正位</jcxm><bszl>乙肝后肝硬化</bszl></RowItem><RowItem><sqdxh>2329891</sqdxh><hzxm>陶信水</hzxm><jcmd>明确诊断</jcmd><lczd>乙肝后肝硬化</lczd><jcxm>电子胃十二指肠镜检查</jcxm><bszl>乙肝后肝硬化</bszl></RowItem><RowItem><sqdxh>2343417</sqdxh><hzxm>陶信水</hzxm><jcmd>明确诊断</jcmd><lczd>乙肝后肝硬化</lczd><jcxm>病理申请</jcxm><bszl>乙肝后肝硬化,乙肝病毒核酸PCR100IU/ml。乙肝表面抗原307.020IU/ml↑，乙肝E抗原1.479S/CO↑，抗HBc抗体9.360S/CO↑。发现HBV-M阳性10年余。</bszl></RowItem></WinResponse>";
        //String RetXml ="<WinResponse><RowItem><blh>149654</blh><brlb>1</brlb><patid>96859</patid><syxh>160560</syxh><qqxh>15692704</qqxh><qqmxxh>6009707</qqmxxh><qqksmc>普放室</qqksmc><ysmc>张占卿</ysmc><qqrq>2020041108:59:34</qqrq><itemcode>19945</itemcode><itemname>胸部正位</itemname><price>70.0000</price><itemqty>1.00</itemqty><itemunit>次</itemunit><url></url><itemtype>0</itemtype><qqlx>0</qqlx></RowItem></WinResponse>";
        ByteArrayInputStream reader = new ByteArrayInputStream(RetXml.getBytes("UTF-8"));
        Map<String, Object> data = XMLToMap.mainMethod(reader);
        Map<String, Object> data4 = (Map<String, Object>) data.get("WinResponse");
        int size = data4.size();
        System.out.print(size);
    }

    public static Map<String, Object> DiGui(Element rootElement) {
        // 对节点进行判断
        int flag = hasGradeChrid(rootElement);
        // 存储本层的map,采用LinkedHashMap,保证的顺序
        Map<String, Object> map_this = new ConcurrentHashMap<String, Object>();
        // 存储子节点的map，采用LinkedHashMap,保证的顺序
        Map<String, Object> map_children = new ConcurrentHashMap<String, Object>();
        // 获取节点迭代器
        Iterator<Element> iterator = rootElement.elementIterator();
        if (flag == 0) {// 说明该节点所有子节点均有子节点,进入递归
            int num = 0;
            while (iterator.hasNext()) {// 依次继续对节点进行操作
                Element childelement = iterator.next();
                map_children = DiGui(childelement);
                map_this.put(childelement.getName() + "_" + num, map_children);
                num++;
            }
        }
        if (flag == 1) {// 说明该节点的所有子节点均无子节点,封装数据
            while (iterator.hasNext()) {
                Element childelement = iterator.next();
                map_this.put(childelement.getName(), (String) childelement.getData());
            }
        }
        if (flag == 2) {// 说明了该节点的子节点有些拥有子节点，有些不拥有
            int nodes = rootElement.elements().size();// 获取子节点个数
            while (nodes >= 1) {
                nodes--;
                int num = 0;// 为了让循环重复的节点，避免了key的冲突
                Element element = iterator.next();
                flag = hasGradeChrid(element);// 对节点进行判断
                if (flag == 1) { // 对于子节点，如果只是普通的子节点，那么直接将数进行封装
                    // 封装如map,String,String
                    map_this.put(element.getName(), element.getData());
                } else { // 非普通子节点，那么进行递归
                    map_children = DiGui(element);
                    map_this.put(element.getName() + "_" + num, map_children);// 为了让循环重复的节点，避免了key的冲突
                }
            }
        }
        return map_this;
    }

    /**
     * 用于判断该节点的类型 0：说明该节点所有子节点均有子节点 1：说明该节点的所有子节点均无子节点 2：说明了该节点的子节点有些拥有子节点，有些不拥有
     *
     * @param rootelement
     * @return
     */
    public static int hasGradeChrid(Element rootelement) {
        int flag = 1;// 初始为1，用与处理对没有子节点的节点进行判断
        StringBuffer flag_arr = new StringBuffer();
        Iterator<Element> iterator = rootelement.elementIterator();
        while (iterator.hasNext()) {
            Element element = iterator.next();// 获取入参rootelement节点的子节点
            // Iterator<Element> iterator_chirld = element.elementIterator();
            if (element.elements().size() > 0) {// 判断是否有子节点
                flag_arr.append("0");
            } else {
                flag_arr.append("1");
            }
        }
        // 如果只包含0，说明该节点所有子节点均有子节点
        if (flag_arr.toString().contains("0")) {
            flag = 0;
        }
        // 如果只包含1，说明该节点的所有子节点均无子节点
        if (flag_arr.toString().contains("1")) {
            flag = 1;
        }
        // 如果同时包含了,0,1,说明了该节点的子节点有些拥有子节点，有些不拥有
        if (flag_arr.toString().contains("0") && flag_arr.toString().contains("1")) {
            flag = 2;
        }
        return flag;
    }


    private static final String HEAD = "head";
    private static final String BODY = "body";

    /**
     * 解析XML字符串
     *
     * @param xml
     * @return
     * @throws DocumentException
     */
    public static Map<String, Object> parseXmlStr(String xml) throws DocumentException {
        Document document = DocumentHelper.parseText(xml);
        Element root = document.getRootElement();
        return parseElement(root);
    }

    /**
     * 解析Element
     *
     * @param root
     * @return
     */
    private static Map<String, Object> parseElement(Element root) {
        String rootName = root.getName();
        Iterator<Element> rootItor = root.elementIterator();
        Map<String, Object> rMap = new ConcurrentHashMap<>();
        List<Map<String, Object>> rList = new ArrayList<>();
        Map<String, Object> rsltMap = null;
        while (rootItor.hasNext()) {
            Element tmpElement = rootItor.next();
            String name = tmpElement.getName();
            if (rsltMap == null || (!HEAD.equals(name) && !BODY.equals(name) && !tmpElement.isTextOnly())) {
                if (!HEAD.equals(name) && !BODY.equals(name) && !tmpElement.isTextOnly() && rsltMap != null) {
                    rList.add(rsltMap);
                }
                rsltMap = new ConcurrentHashMap<>();
            }
            if (!tmpElement.isTextOnly()) {
                Iterator<Element> headItor = tmpElement.elementIterator();
                while (headItor.hasNext()) {
                    Element hElement = headItor.next();
                    if (hElement.isTextOnly()) {
                        rsltMap.put(hElement.getName(), hElement.getTextTrim());
                    } else {
                        rsltMap.putAll(parseElement(hElement));
                    }
                }
            }
        }
        rList.add(rsltMap);
        rMap.put(rootName, rList);
        return rMap;
    }

}
