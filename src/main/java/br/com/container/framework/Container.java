package br.com.container.framework;

import br.com.container.framework.annotations.Component;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class Container {

    private final Map<Class, Supplier> registries = new HashMap();
    private final ContainerRepository repository;

    private Container(ContainerRepository repository) {
        this.repository = repository;
    }

    public static Container getInstance(Class rootClass) {
        var container = new Container(new ContainerRepository());

        try {
            registryComponentsByRootClass(container, rootClass);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao registrar componentes do container!");
        }

        return container;
    }

    private static void registryComponentsByRootClass(Container container, Class rootClass)
            throws IOException, ClassNotFoundException {

        getClasses(rootClass)
                .stream()
                .filter(c -> c.isAnnotationPresent(Component.class))
                .forEach(container::registry);
    }

    private static List<Class> getClasses(Class rootClass) throws IOException, ClassNotFoundException {
        var classLoader = rootClass.getClassLoader();
        var path = rootClass.getPackageName().replace('.', '/');

        var resources = classLoader.getResources(path);

        var dirs = new ArrayList<File>();

        while (resources.hasMoreElements()) {
            var resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }

        var classes = new ArrayList<Class>();

        for (File directory : dirs) {
            classes.addAll(findClasses(directory, rootClass.getPackageName()));
        }

        return classes;
    }

    private static List<Class> findClasses(File directory, String packageName) throws ClassNotFoundException {
        var classes = new ArrayList<Class>();

        if (!directory.exists()) {
            return classes;
        }

        for (File file : directory.listFiles()) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(
                        findClasses(file, packageName + "." + file.getName())
                );
            } else if (file.getName().endsWith(".class")) {
                classes.add(
                        Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6))
                );
            }
        }

        return classes;
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
        if (!registries.containsKey(clazz)) {
            throw new RuntimeException("Nao existe componente para: " + clazz.getName());
        }

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
