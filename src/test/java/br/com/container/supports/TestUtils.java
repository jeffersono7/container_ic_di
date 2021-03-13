package br.com.container.supports;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

public class TestUtils {
    private static final Map<Class, RegistryInternal> registries = new HashMap<>();

    public static <T> T createMock(Class<T> type) {
        var registryInternal = new RegistryInternal();
        registries.put(type, registryInternal);

        InvocationHandler invocationHandler = (Object obj, Method method, Object[] objects) -> {

            registryInternal.registry(method, null);

            return registryInternal.getReturns(method);
        };

        return (T) Proxy.newProxyInstance(type.getClassLoader(), new Class[]{type}, invocationHandler);
    }

    public static <T> T any(Class<T> clazz) {
        return null;
    }

    public static WhenUtils when(Class target, Object returns) {
        var registry = registries.get(target);

        return new WhenUtils(registry);
    }

    public static class WhenUtils {
        private final RegistryInternal registryInternal;

        public WhenUtils(RegistryInternal registryInternal) {
            this.registryInternal = registryInternal;
        }

        public void thenReturn(Object o) {
            var key = registryInternal.registries
                    .keySet()
                    .stream()
                    .filter(k -> registryInternal.registries.get(k) == null)
                    .findFirst()
                    .orElseThrow(RuntimeException::new);

            registryInternal.registries.put(key, o);
        }
    }

    public static class RegistryInternal {
        private final Map<Method, Object> registries = new HashMap();

        public Object registry(Method method, Object returns) {
            if (!registries.containsKey(method)) {
                registries.put(method, returns);
            }

            return registries.getOrDefault(method, null);
        }

        public Object getReturns(Method method) {
            return registries.get(method);
        }
    }
}
