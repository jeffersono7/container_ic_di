package br.com.container.framework;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Container {

    private final Map<Class, Supplier> registries = new HashMap();
    private final ContainerRepository repository;

    private Container(ContainerRepository repository) {
        this.repository = repository;
    }

    public static Container getInstance() {
        return new Container(new ContainerRepository());
    }

    public Container registry(Class clazz) {
        if (Object.class.equals(clazz)) return this;

        registries.put(clazz, instanceSupplier(clazz));

        Arrays.stream(clazz.getInterfaces())
                .forEach(i -> registries.put(i, instanceSupplier(clazz)));

        this.registry(clazz.getSuperclass());

        return this;
    }

    public <T> T get(Class<T> clazz) {
        return (T) registries.get(clazz).get();
    }

    private <T> Supplier<T> instanceSupplier(Class<T> clazz) {

        return () -> repository.get(clazz).orElseGet(instanciate(clazz));
    }

    private <T> Supplier<T> instanciate(Class<T> clazz) {
        return () -> {
            var constructor = Arrays.stream(clazz.getConstructors())
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException());

            var parameterInstances = Arrays.stream(constructor.getParameterTypes())
                    .map(this::get)
                    .collect(Collectors.toList())
                    .toArray();

            try {
                var instance = (T) constructor.newInstance(parameterInstances);

                repository.put(clazz, instance);

                System.out.println("Instanciei um componente -> classe: " + clazz.getName());
                return instance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new UnsupportedOperationException("Nao foi possivel instanciar componente!");
            }
        };
    }
}
