/*
 * Copyright 2024 author - Hoàng Anh Tiến
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 */
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
