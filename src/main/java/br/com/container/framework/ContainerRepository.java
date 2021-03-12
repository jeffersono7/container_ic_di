package br.com.container.framework;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ContainerRepository {

    private final Map<Class, Object> components = new HashMap<>();

    public <T> Optional<T> get(Class<T> clazz) {

        return Optional.ofNullable(components.getOrDefault(clazz, null))
                .filter(clazz::isInstance)
                .map(obj -> (T) obj);
    }

    public void put(Class clazz, Object object) {
        components.put(clazz, object);
    }
}
