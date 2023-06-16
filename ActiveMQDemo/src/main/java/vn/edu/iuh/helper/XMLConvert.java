package vn.edu.iuh.helper;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import java.io.StringReader;
import java.io.StringWriter;

public class XMLConvert<T> {
    private T type;

    public XMLConvert(T type) {
        this.type = type;
    }

    public T xml2Object(String xml) throws JAXBException {
        T sv = null;
        JAXBContext ctx = JAXBContext.newInstance(type.getClass());
        Unmarshaller ms = ctx.createUnmarshaller();
        sv = (T) ms.unmarshal(new StringReader(xml));
        return sv;
    }

    public String object2XML(T obj) throws JAXBException {
        JAXBContext ctx = JAXBContext.newInstance(type.getClass());
        Marshaller ms = ctx.createMarshaller();
        StringWriter sw = new StringWriter();
        ms.marshal(obj, sw);
        return sw.toString();
    }
}
