package io.hoangtien2k3.commons.factory;

import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class UnmarshallerFactory {

    private static Map<Class, Unmarshaller> instance = new HashMap<>();

    public static Unmarshaller getInstance(Class clz) {
        Unmarshaller obj = instance.get(clz);
        if (obj != null) return obj;

        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(clz);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            instance.put(clz, unmarshaller);
            return unmarshaller;
        } catch (JAXBException e) {
            log.error("Init Unmarshaller error", e);
            return null;
        }
    }
}
