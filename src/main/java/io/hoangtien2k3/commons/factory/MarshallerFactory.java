package io.hoangtien2k3.commons.factory;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MarshallerFactory {
    private static Map<Class, Marshaller> instance = new HashMap<>();

    public static String convertObjectToXML(Object obj, Class cls) {
        Marshaller marshaller = instance.get(cls);
        String xmlTxt = "";
        try {
            // create an instance of `JAXBContext`
            // create an instance of `Marshaller`
            if (marshaller == null) {
                marshaller = JAXBContext.newInstance(cls).createMarshaller();
                // enable pretty-print XML output
                marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
                instance.put(cls, marshaller);
            }

            // write XML to `StringWriter`
            StringWriter sw = new StringWriter();

            // convert book object to XML
            marshaller.marshal(obj, sw);

            xmlTxt = sw.toString();

        } catch (JAXBException ex) {
            log.error("Convert Object To XML  fail: ", ex);
        }
        return xmlTxt;
    }
}
