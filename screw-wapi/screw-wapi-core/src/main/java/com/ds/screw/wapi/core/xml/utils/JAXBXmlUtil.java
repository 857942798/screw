package com.ds.screw.wapi.core.xml.utils;

import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.InputStream;
import java.io.StringWriter;

public class JAXBXmlUtil {

    private static final String JAXB_SCHEMA_LOCATION = "http://www.screw.com/wapi /wapi/data-resource-config.xsd";

    public static String toXML(Object obj) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(obj.getClass());
        Marshaller marshaller = context.createMarshaller();
        marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");// //编码格式
        marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);// 是否格式化生成的xml串
        marshaller.setProperty(Marshaller.JAXB_FRAGMENT, false);// 是否省略xm头声明信息
        marshaller.setProperty(Marshaller.JAXB_SCHEMA_LOCATION, JAXB_SCHEMA_LOCATION);// 是否省略xm头声明信息

        StringWriter out = new StringWriter();
        OutputFormat format = new OutputFormat();
        format.setIndent(true);
        format.setNewlines(true);
        format.setNewLineAfterDeclaration(false);
        XMLWriter writer = new XMLWriter(out, format);
        marshaller.marshal(obj, writer);
        return out.toString();
    }

    public static <T> T fromXML(InputStream in, Class<T> valueType) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(valueType);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        return (T) unmarshaller.unmarshal(in);
    }
}
