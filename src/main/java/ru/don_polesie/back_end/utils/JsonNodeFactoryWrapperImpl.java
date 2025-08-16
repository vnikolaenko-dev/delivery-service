package ru.don_polesie.back_end.utils;

import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.stereotype.Service;

@Service
public class JsonNodeFactoryWrapperImpl {
    public static ObjectNode error(String key, String message) {
        ObjectNode n = JsonNodeFactory.instance.objectNode();
        n.put(key, message);
        return n;
    }
}
