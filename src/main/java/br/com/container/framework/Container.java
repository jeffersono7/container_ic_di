package br.com.container.framework;

import br.com.container.framework.annotations.Component;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static br.com.container.framework.ComponentFinder.getClasses;

public class Container {

    private final Map<Class, Supplier> componentRegistries = new HashMap();
    private final ContainerRepository repository;

    private Container(ContainerRepository repository) {
        this.repository = repository;
    }

    public static Container getInstance(Class rootClass) {
        var container = new Container(new ContainerRepository());

        try {
            container.registryComponentsByRootClass(rootClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao registrar componentes do container!");
        }

        return container;
    }

    public Container registry(Class clazz) {
        if (Object.class.equals(clazz)) return this;

        componentRegistries.put(clazz, instanceSupplier(clazz));

        Arrays.stream(clazz.getInterfaces())
                .forEach(i -> componentRegistries.put(i, instanceSupplier(clazz)));

        this.registry(clazz.getSuperclass());

        return this;
    }

    public <T> T get(Class<T> clazz) {
        if (!componentRegistries.containsKey(clazz)) {
            throw new RuntimeException("Nao existe componente para: " + clazz.getName());
        }

        return (T) componentRegistries.get(clazz).get();
    }

    private void registryComponentsByRootClass(Class rootClass)
            throws IOException, ClassNotFoundException {

        getClasses(rootClass)
                .stream()
                .filter(c -> c.isAnnotationPresent(Component.class))
                .forEach(this::registry);
    }

    private <T> Supplier<T> instanceSupplier(Class<T> clazz) {

        return () -> repository.get(clazz).orElseGet(instanciate(clazz));
    }

    private <T> Supplier<T> instanciate(Class<T> clazz) {
        return () -> {
            var constructor = getConstructor(clazz);

            var parameterComponents = getParameterComponents(constructor);

            try {
                var instance = (T) constructor.newInstance(parameterComponents);

                addComponentIntoRepositories(clazz, instance);

                System.out.println("Instanciei um componente -> classe: " + clazz.getName());
                return instance;
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                throw new UnsupportedOperationException("Nao foi possivel instanciar componente!");
            }
        };
    }

    private Object[] getParameterComponents(Constructor<?> constructor) {
        return Arrays.stream(constructor.getParameterTypes())
                .map(this::get)
                .collect(Collectors.toList())
                .toArray();
    }

    private <T> Constructor<?> getConstructor(Class<T> clazz) {
        return Arrays.stream(clazz.getConstructors())
                .findFirst()
                .orElseThrow(() -> new RuntimeException());
    }

    private <T> void addComponentIntoRepositories(Class<T> clazz, T instance) {
        var scope = clazz.getAnnotation(Component.class).scope();

        if (scope.isSingleton()) {
            repository.put(clazz, instance);
        }
    }
}
