package com.axisrooms.hcr.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import javax.xml.bind.JAXB;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

@Slf4j
public class MUUtils {

    public static <T> String marshal(T t) {
        StringWriter stringWriter = new StringWriter();
        JAXB.marshal(t, stringWriter);
        return stringWriter.toString();
    }

    public static <T> T unmarshal(String string, Class<T> clazz) {
        return JAXB.unmarshal(new StringReader(string), clazz);
    }

    public static <T> T unMarshal(String string, Class<T> clazz) throws IOException {
        return new ObjectMapper().readValue(string, clazz);
    }
}
