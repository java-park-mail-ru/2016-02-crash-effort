package main;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by vladislav on 25.03.16.
 */
public class Context {
    private Map<Class<?>, Object> context = new ConcurrentHashMap<>();

    public void put(Object object) {
        Class<?> clazz = object.getClass();
        if (context.containsKey(clazz))
            throw new IllegalArgumentException("Duplicate key");

        context.put(clazz, object);
    }

    public Object get(Class<?> clazz) {
        return context.get(clazz);
    }
}
